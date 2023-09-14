package io.github.erp.web.rest;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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
import io.github.erp.domain.RemittanceFlag;
import io.github.erp.domain.enumeration.RemittanceType;
import io.github.erp.domain.enumeration.RemittanceTypeFlag;
import io.github.erp.repository.RemittanceFlagRepository;
import io.github.erp.repository.search.RemittanceFlagSearchRepository;
import io.github.erp.service.criteria.RemittanceFlagCriteria;
import io.github.erp.service.dto.RemittanceFlagDTO;
import io.github.erp.service.mapper.RemittanceFlagMapper;
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
 * Integration tests for the {@link RemittanceFlagResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RemittanceFlagResourceIT {

    private static final RemittanceTypeFlag DEFAULT_REMITTANCE_TYPE_FLAG = RemittanceTypeFlag.RMTIN;
    private static final RemittanceTypeFlag UPDATED_REMITTANCE_TYPE_FLAG = RemittanceTypeFlag.RMTOUT;

    private static final RemittanceType DEFAULT_REMITTANCE_TYPE = RemittanceType.INFLOWS;
    private static final RemittanceType UPDATED_REMITTANCE_TYPE = RemittanceType.OUTFLOWS;

    private static final String DEFAULT_REMITTANCE_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_REMITTANCE_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/remittance-flags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/remittance-flags";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RemittanceFlagRepository remittanceFlagRepository;

    @Autowired
    private RemittanceFlagMapper remittanceFlagMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RemittanceFlagSearchRepositoryMockConfiguration
     */
    @Autowired
    private RemittanceFlagSearchRepository mockRemittanceFlagSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRemittanceFlagMockMvc;

    private RemittanceFlag remittanceFlag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RemittanceFlag createEntity(EntityManager em) {
        RemittanceFlag remittanceFlag = new RemittanceFlag()
            .remittanceTypeFlag(DEFAULT_REMITTANCE_TYPE_FLAG)
            .remittanceType(DEFAULT_REMITTANCE_TYPE)
            .remittanceTypeDetails(DEFAULT_REMITTANCE_TYPE_DETAILS);
        return remittanceFlag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RemittanceFlag createUpdatedEntity(EntityManager em) {
        RemittanceFlag remittanceFlag = new RemittanceFlag()
            .remittanceTypeFlag(UPDATED_REMITTANCE_TYPE_FLAG)
            .remittanceType(UPDATED_REMITTANCE_TYPE)
            .remittanceTypeDetails(UPDATED_REMITTANCE_TYPE_DETAILS);
        return remittanceFlag;
    }

    @BeforeEach
    public void initTest() {
        remittanceFlag = createEntity(em);
    }

    @Test
    @Transactional
    void createRemittanceFlag() throws Exception {
        int databaseSizeBeforeCreate = remittanceFlagRepository.findAll().size();
        // Create the RemittanceFlag
        RemittanceFlagDTO remittanceFlagDTO = remittanceFlagMapper.toDto(remittanceFlag);
        restRemittanceFlagMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(remittanceFlagDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RemittanceFlag in the database
        List<RemittanceFlag> remittanceFlagList = remittanceFlagRepository.findAll();
        assertThat(remittanceFlagList).hasSize(databaseSizeBeforeCreate + 1);
        RemittanceFlag testRemittanceFlag = remittanceFlagList.get(remittanceFlagList.size() - 1);
        assertThat(testRemittanceFlag.getRemittanceTypeFlag()).isEqualTo(DEFAULT_REMITTANCE_TYPE_FLAG);
        assertThat(testRemittanceFlag.getRemittanceType()).isEqualTo(DEFAULT_REMITTANCE_TYPE);
        assertThat(testRemittanceFlag.getRemittanceTypeDetails()).isEqualTo(DEFAULT_REMITTANCE_TYPE_DETAILS);

        // Validate the RemittanceFlag in Elasticsearch
        verify(mockRemittanceFlagSearchRepository, times(1)).save(testRemittanceFlag);
    }

    @Test
    @Transactional
    void createRemittanceFlagWithExistingId() throws Exception {
        // Create the RemittanceFlag with an existing ID
        remittanceFlag.setId(1L);
        RemittanceFlagDTO remittanceFlagDTO = remittanceFlagMapper.toDto(remittanceFlag);

        int databaseSizeBeforeCreate = remittanceFlagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRemittanceFlagMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(remittanceFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RemittanceFlag in the database
        List<RemittanceFlag> remittanceFlagList = remittanceFlagRepository.findAll();
        assertThat(remittanceFlagList).hasSize(databaseSizeBeforeCreate);

        // Validate the RemittanceFlag in Elasticsearch
        verify(mockRemittanceFlagSearchRepository, times(0)).save(remittanceFlag);
    }

    @Test
    @Transactional
    void checkRemittanceTypeFlagIsRequired() throws Exception {
        int databaseSizeBeforeTest = remittanceFlagRepository.findAll().size();
        // set the field null
        remittanceFlag.setRemittanceTypeFlag(null);

        // Create the RemittanceFlag, which fails.
        RemittanceFlagDTO remittanceFlagDTO = remittanceFlagMapper.toDto(remittanceFlag);

        restRemittanceFlagMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(remittanceFlagDTO))
            )
            .andExpect(status().isBadRequest());

        List<RemittanceFlag> remittanceFlagList = remittanceFlagRepository.findAll();
        assertThat(remittanceFlagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRemittanceTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = remittanceFlagRepository.findAll().size();
        // set the field null
        remittanceFlag.setRemittanceType(null);

        // Create the RemittanceFlag, which fails.
        RemittanceFlagDTO remittanceFlagDTO = remittanceFlagMapper.toDto(remittanceFlag);

        restRemittanceFlagMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(remittanceFlagDTO))
            )
            .andExpect(status().isBadRequest());

        List<RemittanceFlag> remittanceFlagList = remittanceFlagRepository.findAll();
        assertThat(remittanceFlagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRemittanceFlags() throws Exception {
        // Initialize the database
        remittanceFlagRepository.saveAndFlush(remittanceFlag);

        // Get all the remittanceFlagList
        restRemittanceFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(remittanceFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].remittanceTypeFlag").value(hasItem(DEFAULT_REMITTANCE_TYPE_FLAG.toString())))
            .andExpect(jsonPath("$.[*].remittanceType").value(hasItem(DEFAULT_REMITTANCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].remittanceTypeDetails").value(hasItem(DEFAULT_REMITTANCE_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getRemittanceFlag() throws Exception {
        // Initialize the database
        remittanceFlagRepository.saveAndFlush(remittanceFlag);

        // Get the remittanceFlag
        restRemittanceFlagMockMvc
            .perform(get(ENTITY_API_URL_ID, remittanceFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(remittanceFlag.getId().intValue()))
            .andExpect(jsonPath("$.remittanceTypeFlag").value(DEFAULT_REMITTANCE_TYPE_FLAG.toString()))
            .andExpect(jsonPath("$.remittanceType").value(DEFAULT_REMITTANCE_TYPE.toString()))
            .andExpect(jsonPath("$.remittanceTypeDetails").value(DEFAULT_REMITTANCE_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getRemittanceFlagsByIdFiltering() throws Exception {
        // Initialize the database
        remittanceFlagRepository.saveAndFlush(remittanceFlag);

        Long id = remittanceFlag.getId();

        defaultRemittanceFlagShouldBeFound("id.equals=" + id);
        defaultRemittanceFlagShouldNotBeFound("id.notEquals=" + id);

        defaultRemittanceFlagShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRemittanceFlagShouldNotBeFound("id.greaterThan=" + id);

        defaultRemittanceFlagShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRemittanceFlagShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRemittanceFlagsByRemittanceTypeFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        remittanceFlagRepository.saveAndFlush(remittanceFlag);

        // Get all the remittanceFlagList where remittanceTypeFlag equals to DEFAULT_REMITTANCE_TYPE_FLAG
        defaultRemittanceFlagShouldBeFound("remittanceTypeFlag.equals=" + DEFAULT_REMITTANCE_TYPE_FLAG);

        // Get all the remittanceFlagList where remittanceTypeFlag equals to UPDATED_REMITTANCE_TYPE_FLAG
        defaultRemittanceFlagShouldNotBeFound("remittanceTypeFlag.equals=" + UPDATED_REMITTANCE_TYPE_FLAG);
    }

    @Test
    @Transactional
    void getAllRemittanceFlagsByRemittanceTypeFlagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        remittanceFlagRepository.saveAndFlush(remittanceFlag);

        // Get all the remittanceFlagList where remittanceTypeFlag not equals to DEFAULT_REMITTANCE_TYPE_FLAG
        defaultRemittanceFlagShouldNotBeFound("remittanceTypeFlag.notEquals=" + DEFAULT_REMITTANCE_TYPE_FLAG);

        // Get all the remittanceFlagList where remittanceTypeFlag not equals to UPDATED_REMITTANCE_TYPE_FLAG
        defaultRemittanceFlagShouldBeFound("remittanceTypeFlag.notEquals=" + UPDATED_REMITTANCE_TYPE_FLAG);
    }

    @Test
    @Transactional
    void getAllRemittanceFlagsByRemittanceTypeFlagIsInShouldWork() throws Exception {
        // Initialize the database
        remittanceFlagRepository.saveAndFlush(remittanceFlag);

        // Get all the remittanceFlagList where remittanceTypeFlag in DEFAULT_REMITTANCE_TYPE_FLAG or UPDATED_REMITTANCE_TYPE_FLAG
        defaultRemittanceFlagShouldBeFound("remittanceTypeFlag.in=" + DEFAULT_REMITTANCE_TYPE_FLAG + "," + UPDATED_REMITTANCE_TYPE_FLAG);

        // Get all the remittanceFlagList where remittanceTypeFlag equals to UPDATED_REMITTANCE_TYPE_FLAG
        defaultRemittanceFlagShouldNotBeFound("remittanceTypeFlag.in=" + UPDATED_REMITTANCE_TYPE_FLAG);
    }

    @Test
    @Transactional
    void getAllRemittanceFlagsByRemittanceTypeFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        remittanceFlagRepository.saveAndFlush(remittanceFlag);

        // Get all the remittanceFlagList where remittanceTypeFlag is not null
        defaultRemittanceFlagShouldBeFound("remittanceTypeFlag.specified=true");

        // Get all the remittanceFlagList where remittanceTypeFlag is null
        defaultRemittanceFlagShouldNotBeFound("remittanceTypeFlag.specified=false");
    }

    @Test
    @Transactional
    void getAllRemittanceFlagsByRemittanceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        remittanceFlagRepository.saveAndFlush(remittanceFlag);

        // Get all the remittanceFlagList where remittanceType equals to DEFAULT_REMITTANCE_TYPE
        defaultRemittanceFlagShouldBeFound("remittanceType.equals=" + DEFAULT_REMITTANCE_TYPE);

        // Get all the remittanceFlagList where remittanceType equals to UPDATED_REMITTANCE_TYPE
        defaultRemittanceFlagShouldNotBeFound("remittanceType.equals=" + UPDATED_REMITTANCE_TYPE);
    }

    @Test
    @Transactional
    void getAllRemittanceFlagsByRemittanceTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        remittanceFlagRepository.saveAndFlush(remittanceFlag);

        // Get all the remittanceFlagList where remittanceType not equals to DEFAULT_REMITTANCE_TYPE
        defaultRemittanceFlagShouldNotBeFound("remittanceType.notEquals=" + DEFAULT_REMITTANCE_TYPE);

        // Get all the remittanceFlagList where remittanceType not equals to UPDATED_REMITTANCE_TYPE
        defaultRemittanceFlagShouldBeFound("remittanceType.notEquals=" + UPDATED_REMITTANCE_TYPE);
    }

    @Test
    @Transactional
    void getAllRemittanceFlagsByRemittanceTypeIsInShouldWork() throws Exception {
        // Initialize the database
        remittanceFlagRepository.saveAndFlush(remittanceFlag);

        // Get all the remittanceFlagList where remittanceType in DEFAULT_REMITTANCE_TYPE or UPDATED_REMITTANCE_TYPE
        defaultRemittanceFlagShouldBeFound("remittanceType.in=" + DEFAULT_REMITTANCE_TYPE + "," + UPDATED_REMITTANCE_TYPE);

        // Get all the remittanceFlagList where remittanceType equals to UPDATED_REMITTANCE_TYPE
        defaultRemittanceFlagShouldNotBeFound("remittanceType.in=" + UPDATED_REMITTANCE_TYPE);
    }

    @Test
    @Transactional
    void getAllRemittanceFlagsByRemittanceTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        remittanceFlagRepository.saveAndFlush(remittanceFlag);

        // Get all the remittanceFlagList where remittanceType is not null
        defaultRemittanceFlagShouldBeFound("remittanceType.specified=true");

        // Get all the remittanceFlagList where remittanceType is null
        defaultRemittanceFlagShouldNotBeFound("remittanceType.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRemittanceFlagShouldBeFound(String filter) throws Exception {
        restRemittanceFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(remittanceFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].remittanceTypeFlag").value(hasItem(DEFAULT_REMITTANCE_TYPE_FLAG.toString())))
            .andExpect(jsonPath("$.[*].remittanceType").value(hasItem(DEFAULT_REMITTANCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].remittanceTypeDetails").value(hasItem(DEFAULT_REMITTANCE_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restRemittanceFlagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRemittanceFlagShouldNotBeFound(String filter) throws Exception {
        restRemittanceFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRemittanceFlagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRemittanceFlag() throws Exception {
        // Get the remittanceFlag
        restRemittanceFlagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRemittanceFlag() throws Exception {
        // Initialize the database
        remittanceFlagRepository.saveAndFlush(remittanceFlag);

        int databaseSizeBeforeUpdate = remittanceFlagRepository.findAll().size();

        // Update the remittanceFlag
        RemittanceFlag updatedRemittanceFlag = remittanceFlagRepository.findById(remittanceFlag.getId()).get();
        // Disconnect from session so that the updates on updatedRemittanceFlag are not directly saved in db
        em.detach(updatedRemittanceFlag);
        updatedRemittanceFlag
            .remittanceTypeFlag(UPDATED_REMITTANCE_TYPE_FLAG)
            .remittanceType(UPDATED_REMITTANCE_TYPE)
            .remittanceTypeDetails(UPDATED_REMITTANCE_TYPE_DETAILS);
        RemittanceFlagDTO remittanceFlagDTO = remittanceFlagMapper.toDto(updatedRemittanceFlag);

        restRemittanceFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, remittanceFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(remittanceFlagDTO))
            )
            .andExpect(status().isOk());

        // Validate the RemittanceFlag in the database
        List<RemittanceFlag> remittanceFlagList = remittanceFlagRepository.findAll();
        assertThat(remittanceFlagList).hasSize(databaseSizeBeforeUpdate);
        RemittanceFlag testRemittanceFlag = remittanceFlagList.get(remittanceFlagList.size() - 1);
        assertThat(testRemittanceFlag.getRemittanceTypeFlag()).isEqualTo(UPDATED_REMITTANCE_TYPE_FLAG);
        assertThat(testRemittanceFlag.getRemittanceType()).isEqualTo(UPDATED_REMITTANCE_TYPE);
        assertThat(testRemittanceFlag.getRemittanceTypeDetails()).isEqualTo(UPDATED_REMITTANCE_TYPE_DETAILS);

        // Validate the RemittanceFlag in Elasticsearch
        verify(mockRemittanceFlagSearchRepository).save(testRemittanceFlag);
    }

    @Test
    @Transactional
    void putNonExistingRemittanceFlag() throws Exception {
        int databaseSizeBeforeUpdate = remittanceFlagRepository.findAll().size();
        remittanceFlag.setId(count.incrementAndGet());

        // Create the RemittanceFlag
        RemittanceFlagDTO remittanceFlagDTO = remittanceFlagMapper.toDto(remittanceFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRemittanceFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, remittanceFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(remittanceFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RemittanceFlag in the database
        List<RemittanceFlag> remittanceFlagList = remittanceFlagRepository.findAll();
        assertThat(remittanceFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RemittanceFlag in Elasticsearch
        verify(mockRemittanceFlagSearchRepository, times(0)).save(remittanceFlag);
    }

    @Test
    @Transactional
    void putWithIdMismatchRemittanceFlag() throws Exception {
        int databaseSizeBeforeUpdate = remittanceFlagRepository.findAll().size();
        remittanceFlag.setId(count.incrementAndGet());

        // Create the RemittanceFlag
        RemittanceFlagDTO remittanceFlagDTO = remittanceFlagMapper.toDto(remittanceFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRemittanceFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(remittanceFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RemittanceFlag in the database
        List<RemittanceFlag> remittanceFlagList = remittanceFlagRepository.findAll();
        assertThat(remittanceFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RemittanceFlag in Elasticsearch
        verify(mockRemittanceFlagSearchRepository, times(0)).save(remittanceFlag);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRemittanceFlag() throws Exception {
        int databaseSizeBeforeUpdate = remittanceFlagRepository.findAll().size();
        remittanceFlag.setId(count.incrementAndGet());

        // Create the RemittanceFlag
        RemittanceFlagDTO remittanceFlagDTO = remittanceFlagMapper.toDto(remittanceFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRemittanceFlagMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(remittanceFlagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RemittanceFlag in the database
        List<RemittanceFlag> remittanceFlagList = remittanceFlagRepository.findAll();
        assertThat(remittanceFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RemittanceFlag in Elasticsearch
        verify(mockRemittanceFlagSearchRepository, times(0)).save(remittanceFlag);
    }

    @Test
    @Transactional
    void partialUpdateRemittanceFlagWithPatch() throws Exception {
        // Initialize the database
        remittanceFlagRepository.saveAndFlush(remittanceFlag);

        int databaseSizeBeforeUpdate = remittanceFlagRepository.findAll().size();

        // Update the remittanceFlag using partial update
        RemittanceFlag partialUpdatedRemittanceFlag = new RemittanceFlag();
        partialUpdatedRemittanceFlag.setId(remittanceFlag.getId());

        partialUpdatedRemittanceFlag.remittanceTypeDetails(UPDATED_REMITTANCE_TYPE_DETAILS);

        restRemittanceFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRemittanceFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRemittanceFlag))
            )
            .andExpect(status().isOk());

        // Validate the RemittanceFlag in the database
        List<RemittanceFlag> remittanceFlagList = remittanceFlagRepository.findAll();
        assertThat(remittanceFlagList).hasSize(databaseSizeBeforeUpdate);
        RemittanceFlag testRemittanceFlag = remittanceFlagList.get(remittanceFlagList.size() - 1);
        assertThat(testRemittanceFlag.getRemittanceTypeFlag()).isEqualTo(DEFAULT_REMITTANCE_TYPE_FLAG);
        assertThat(testRemittanceFlag.getRemittanceType()).isEqualTo(DEFAULT_REMITTANCE_TYPE);
        assertThat(testRemittanceFlag.getRemittanceTypeDetails()).isEqualTo(UPDATED_REMITTANCE_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateRemittanceFlagWithPatch() throws Exception {
        // Initialize the database
        remittanceFlagRepository.saveAndFlush(remittanceFlag);

        int databaseSizeBeforeUpdate = remittanceFlagRepository.findAll().size();

        // Update the remittanceFlag using partial update
        RemittanceFlag partialUpdatedRemittanceFlag = new RemittanceFlag();
        partialUpdatedRemittanceFlag.setId(remittanceFlag.getId());

        partialUpdatedRemittanceFlag
            .remittanceTypeFlag(UPDATED_REMITTANCE_TYPE_FLAG)
            .remittanceType(UPDATED_REMITTANCE_TYPE)
            .remittanceTypeDetails(UPDATED_REMITTANCE_TYPE_DETAILS);

        restRemittanceFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRemittanceFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRemittanceFlag))
            )
            .andExpect(status().isOk());

        // Validate the RemittanceFlag in the database
        List<RemittanceFlag> remittanceFlagList = remittanceFlagRepository.findAll();
        assertThat(remittanceFlagList).hasSize(databaseSizeBeforeUpdate);
        RemittanceFlag testRemittanceFlag = remittanceFlagList.get(remittanceFlagList.size() - 1);
        assertThat(testRemittanceFlag.getRemittanceTypeFlag()).isEqualTo(UPDATED_REMITTANCE_TYPE_FLAG);
        assertThat(testRemittanceFlag.getRemittanceType()).isEqualTo(UPDATED_REMITTANCE_TYPE);
        assertThat(testRemittanceFlag.getRemittanceTypeDetails()).isEqualTo(UPDATED_REMITTANCE_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingRemittanceFlag() throws Exception {
        int databaseSizeBeforeUpdate = remittanceFlagRepository.findAll().size();
        remittanceFlag.setId(count.incrementAndGet());

        // Create the RemittanceFlag
        RemittanceFlagDTO remittanceFlagDTO = remittanceFlagMapper.toDto(remittanceFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRemittanceFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, remittanceFlagDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(remittanceFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RemittanceFlag in the database
        List<RemittanceFlag> remittanceFlagList = remittanceFlagRepository.findAll();
        assertThat(remittanceFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RemittanceFlag in Elasticsearch
        verify(mockRemittanceFlagSearchRepository, times(0)).save(remittanceFlag);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRemittanceFlag() throws Exception {
        int databaseSizeBeforeUpdate = remittanceFlagRepository.findAll().size();
        remittanceFlag.setId(count.incrementAndGet());

        // Create the RemittanceFlag
        RemittanceFlagDTO remittanceFlagDTO = remittanceFlagMapper.toDto(remittanceFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRemittanceFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(remittanceFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RemittanceFlag in the database
        List<RemittanceFlag> remittanceFlagList = remittanceFlagRepository.findAll();
        assertThat(remittanceFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RemittanceFlag in Elasticsearch
        verify(mockRemittanceFlagSearchRepository, times(0)).save(remittanceFlag);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRemittanceFlag() throws Exception {
        int databaseSizeBeforeUpdate = remittanceFlagRepository.findAll().size();
        remittanceFlag.setId(count.incrementAndGet());

        // Create the RemittanceFlag
        RemittanceFlagDTO remittanceFlagDTO = remittanceFlagMapper.toDto(remittanceFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRemittanceFlagMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(remittanceFlagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RemittanceFlag in the database
        List<RemittanceFlag> remittanceFlagList = remittanceFlagRepository.findAll();
        assertThat(remittanceFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RemittanceFlag in Elasticsearch
        verify(mockRemittanceFlagSearchRepository, times(0)).save(remittanceFlag);
    }

    @Test
    @Transactional
    void deleteRemittanceFlag() throws Exception {
        // Initialize the database
        remittanceFlagRepository.saveAndFlush(remittanceFlag);

        int databaseSizeBeforeDelete = remittanceFlagRepository.findAll().size();

        // Delete the remittanceFlag
        restRemittanceFlagMockMvc
            .perform(delete(ENTITY_API_URL_ID, remittanceFlag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RemittanceFlag> remittanceFlagList = remittanceFlagRepository.findAll();
        assertThat(remittanceFlagList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RemittanceFlag in Elasticsearch
        verify(mockRemittanceFlagSearchRepository, times(1)).deleteById(remittanceFlag.getId());
    }

    @Test
    @Transactional
    void searchRemittanceFlag() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        remittanceFlagRepository.saveAndFlush(remittanceFlag);
        when(mockRemittanceFlagSearchRepository.search("id:" + remittanceFlag.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(remittanceFlag), PageRequest.of(0, 1), 1));

        // Search the remittanceFlag
        restRemittanceFlagMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + remittanceFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(remittanceFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].remittanceTypeFlag").value(hasItem(DEFAULT_REMITTANCE_TYPE_FLAG.toString())))
            .andExpect(jsonPath("$.[*].remittanceType").value(hasItem(DEFAULT_REMITTANCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].remittanceTypeDetails").value(hasItem(DEFAULT_REMITTANCE_TYPE_DETAILS.toString())));
    }
}
