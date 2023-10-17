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
import io.github.erp.domain.ContractStatus;
import io.github.erp.repository.ContractStatusRepository;
import io.github.erp.repository.search.ContractStatusSearchRepository;
import io.github.erp.service.criteria.ContractStatusCriteria;
import io.github.erp.service.dto.ContractStatusDTO;
import io.github.erp.service.mapper.ContractStatusMapper;
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
 * Integration tests for the {@link ContractStatusResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContractStatusResourceIT {

    private static final String DEFAULT_CONTRACT_STATUS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_STATUS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRACT_STATUS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_STATUS_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRACT_STATUS_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_STATUS_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contract-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/contract-statuses";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContractStatusRepository contractStatusRepository;

    @Autowired
    private ContractStatusMapper contractStatusMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ContractStatusSearchRepositoryMockConfiguration
     */
    @Autowired
    private ContractStatusSearchRepository mockContractStatusSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractStatusMockMvc;

    private ContractStatus contractStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractStatus createEntity(EntityManager em) {
        ContractStatus contractStatus = new ContractStatus()
            .contractStatusCode(DEFAULT_CONTRACT_STATUS_CODE)
            .contractStatusType(DEFAULT_CONTRACT_STATUS_TYPE)
            .contractStatusTypeDescription(DEFAULT_CONTRACT_STATUS_TYPE_DESCRIPTION);
        return contractStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractStatus createUpdatedEntity(EntityManager em) {
        ContractStatus contractStatus = new ContractStatus()
            .contractStatusCode(UPDATED_CONTRACT_STATUS_CODE)
            .contractStatusType(UPDATED_CONTRACT_STATUS_TYPE)
            .contractStatusTypeDescription(UPDATED_CONTRACT_STATUS_TYPE_DESCRIPTION);
        return contractStatus;
    }

    @BeforeEach
    public void initTest() {
        contractStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createContractStatus() throws Exception {
        int databaseSizeBeforeCreate = contractStatusRepository.findAll().size();
        // Create the ContractStatus
        ContractStatusDTO contractStatusDTO = contractStatusMapper.toDto(contractStatus);
        restContractStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractStatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContractStatus in the database
        List<ContractStatus> contractStatusList = contractStatusRepository.findAll();
        assertThat(contractStatusList).hasSize(databaseSizeBeforeCreate + 1);
        ContractStatus testContractStatus = contractStatusList.get(contractStatusList.size() - 1);
        assertThat(testContractStatus.getContractStatusCode()).isEqualTo(DEFAULT_CONTRACT_STATUS_CODE);
        assertThat(testContractStatus.getContractStatusType()).isEqualTo(DEFAULT_CONTRACT_STATUS_TYPE);
        assertThat(testContractStatus.getContractStatusTypeDescription()).isEqualTo(DEFAULT_CONTRACT_STATUS_TYPE_DESCRIPTION);

        // Validate the ContractStatus in Elasticsearch
        verify(mockContractStatusSearchRepository, times(1)).save(testContractStatus);
    }

    @Test
    @Transactional
    void createContractStatusWithExistingId() throws Exception {
        // Create the ContractStatus with an existing ID
        contractStatus.setId(1L);
        ContractStatusDTO contractStatusDTO = contractStatusMapper.toDto(contractStatus);

        int databaseSizeBeforeCreate = contractStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractStatus in the database
        List<ContractStatus> contractStatusList = contractStatusRepository.findAll();
        assertThat(contractStatusList).hasSize(databaseSizeBeforeCreate);

        // Validate the ContractStatus in Elasticsearch
        verify(mockContractStatusSearchRepository, times(0)).save(contractStatus);
    }

    @Test
    @Transactional
    void checkContractStatusCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractStatusRepository.findAll().size();
        // set the field null
        contractStatus.setContractStatusCode(null);

        // Create the ContractStatus, which fails.
        ContractStatusDTO contractStatusDTO = contractStatusMapper.toDto(contractStatus);

        restContractStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContractStatus> contractStatusList = contractStatusRepository.findAll();
        assertThat(contractStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContractStatusTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractStatusRepository.findAll().size();
        // set the field null
        contractStatus.setContractStatusType(null);

        // Create the ContractStatus, which fails.
        ContractStatusDTO contractStatusDTO = contractStatusMapper.toDto(contractStatus);

        restContractStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContractStatus> contractStatusList = contractStatusRepository.findAll();
        assertThat(contractStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContractStatuses() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        // Get all the contractStatusList
        restContractStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].contractStatusCode").value(hasItem(DEFAULT_CONTRACT_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].contractStatusType").value(hasItem(DEFAULT_CONTRACT_STATUS_TYPE)))
            .andExpect(jsonPath("$.[*].contractStatusTypeDescription").value(hasItem(DEFAULT_CONTRACT_STATUS_TYPE_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getContractStatus() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        // Get the contractStatus
        restContractStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, contractStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contractStatus.getId().intValue()))
            .andExpect(jsonPath("$.contractStatusCode").value(DEFAULT_CONTRACT_STATUS_CODE))
            .andExpect(jsonPath("$.contractStatusType").value(DEFAULT_CONTRACT_STATUS_TYPE))
            .andExpect(jsonPath("$.contractStatusTypeDescription").value(DEFAULT_CONTRACT_STATUS_TYPE_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getContractStatusesByIdFiltering() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        Long id = contractStatus.getId();

        defaultContractStatusShouldBeFound("id.equals=" + id);
        defaultContractStatusShouldNotBeFound("id.notEquals=" + id);

        defaultContractStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContractStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultContractStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContractStatusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContractStatusesByContractStatusCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        // Get all the contractStatusList where contractStatusCode equals to DEFAULT_CONTRACT_STATUS_CODE
        defaultContractStatusShouldBeFound("contractStatusCode.equals=" + DEFAULT_CONTRACT_STATUS_CODE);

        // Get all the contractStatusList where contractStatusCode equals to UPDATED_CONTRACT_STATUS_CODE
        defaultContractStatusShouldNotBeFound("contractStatusCode.equals=" + UPDATED_CONTRACT_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllContractStatusesByContractStatusCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        // Get all the contractStatusList where contractStatusCode not equals to DEFAULT_CONTRACT_STATUS_CODE
        defaultContractStatusShouldNotBeFound("contractStatusCode.notEquals=" + DEFAULT_CONTRACT_STATUS_CODE);

        // Get all the contractStatusList where contractStatusCode not equals to UPDATED_CONTRACT_STATUS_CODE
        defaultContractStatusShouldBeFound("contractStatusCode.notEquals=" + UPDATED_CONTRACT_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllContractStatusesByContractStatusCodeIsInShouldWork() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        // Get all the contractStatusList where contractStatusCode in DEFAULT_CONTRACT_STATUS_CODE or UPDATED_CONTRACT_STATUS_CODE
        defaultContractStatusShouldBeFound("contractStatusCode.in=" + DEFAULT_CONTRACT_STATUS_CODE + "," + UPDATED_CONTRACT_STATUS_CODE);

        // Get all the contractStatusList where contractStatusCode equals to UPDATED_CONTRACT_STATUS_CODE
        defaultContractStatusShouldNotBeFound("contractStatusCode.in=" + UPDATED_CONTRACT_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllContractStatusesByContractStatusCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        // Get all the contractStatusList where contractStatusCode is not null
        defaultContractStatusShouldBeFound("contractStatusCode.specified=true");

        // Get all the contractStatusList where contractStatusCode is null
        defaultContractStatusShouldNotBeFound("contractStatusCode.specified=false");
    }

    @Test
    @Transactional
    void getAllContractStatusesByContractStatusCodeContainsSomething() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        // Get all the contractStatusList where contractStatusCode contains DEFAULT_CONTRACT_STATUS_CODE
        defaultContractStatusShouldBeFound("contractStatusCode.contains=" + DEFAULT_CONTRACT_STATUS_CODE);

        // Get all the contractStatusList where contractStatusCode contains UPDATED_CONTRACT_STATUS_CODE
        defaultContractStatusShouldNotBeFound("contractStatusCode.contains=" + UPDATED_CONTRACT_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllContractStatusesByContractStatusCodeNotContainsSomething() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        // Get all the contractStatusList where contractStatusCode does not contain DEFAULT_CONTRACT_STATUS_CODE
        defaultContractStatusShouldNotBeFound("contractStatusCode.doesNotContain=" + DEFAULT_CONTRACT_STATUS_CODE);

        // Get all the contractStatusList where contractStatusCode does not contain UPDATED_CONTRACT_STATUS_CODE
        defaultContractStatusShouldBeFound("contractStatusCode.doesNotContain=" + UPDATED_CONTRACT_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllContractStatusesByContractStatusTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        // Get all the contractStatusList where contractStatusType equals to DEFAULT_CONTRACT_STATUS_TYPE
        defaultContractStatusShouldBeFound("contractStatusType.equals=" + DEFAULT_CONTRACT_STATUS_TYPE);

        // Get all the contractStatusList where contractStatusType equals to UPDATED_CONTRACT_STATUS_TYPE
        defaultContractStatusShouldNotBeFound("contractStatusType.equals=" + UPDATED_CONTRACT_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllContractStatusesByContractStatusTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        // Get all the contractStatusList where contractStatusType not equals to DEFAULT_CONTRACT_STATUS_TYPE
        defaultContractStatusShouldNotBeFound("contractStatusType.notEquals=" + DEFAULT_CONTRACT_STATUS_TYPE);

        // Get all the contractStatusList where contractStatusType not equals to UPDATED_CONTRACT_STATUS_TYPE
        defaultContractStatusShouldBeFound("contractStatusType.notEquals=" + UPDATED_CONTRACT_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllContractStatusesByContractStatusTypeIsInShouldWork() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        // Get all the contractStatusList where contractStatusType in DEFAULT_CONTRACT_STATUS_TYPE or UPDATED_CONTRACT_STATUS_TYPE
        defaultContractStatusShouldBeFound("contractStatusType.in=" + DEFAULT_CONTRACT_STATUS_TYPE + "," + UPDATED_CONTRACT_STATUS_TYPE);

        // Get all the contractStatusList where contractStatusType equals to UPDATED_CONTRACT_STATUS_TYPE
        defaultContractStatusShouldNotBeFound("contractStatusType.in=" + UPDATED_CONTRACT_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllContractStatusesByContractStatusTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        // Get all the contractStatusList where contractStatusType is not null
        defaultContractStatusShouldBeFound("contractStatusType.specified=true");

        // Get all the contractStatusList where contractStatusType is null
        defaultContractStatusShouldNotBeFound("contractStatusType.specified=false");
    }

    @Test
    @Transactional
    void getAllContractStatusesByContractStatusTypeContainsSomething() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        // Get all the contractStatusList where contractStatusType contains DEFAULT_CONTRACT_STATUS_TYPE
        defaultContractStatusShouldBeFound("contractStatusType.contains=" + DEFAULT_CONTRACT_STATUS_TYPE);

        // Get all the contractStatusList where contractStatusType contains UPDATED_CONTRACT_STATUS_TYPE
        defaultContractStatusShouldNotBeFound("contractStatusType.contains=" + UPDATED_CONTRACT_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllContractStatusesByContractStatusTypeNotContainsSomething() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        // Get all the contractStatusList where contractStatusType does not contain DEFAULT_CONTRACT_STATUS_TYPE
        defaultContractStatusShouldNotBeFound("contractStatusType.doesNotContain=" + DEFAULT_CONTRACT_STATUS_TYPE);

        // Get all the contractStatusList where contractStatusType does not contain UPDATED_CONTRACT_STATUS_TYPE
        defaultContractStatusShouldBeFound("contractStatusType.doesNotContain=" + UPDATED_CONTRACT_STATUS_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContractStatusShouldBeFound(String filter) throws Exception {
        restContractStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].contractStatusCode").value(hasItem(DEFAULT_CONTRACT_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].contractStatusType").value(hasItem(DEFAULT_CONTRACT_STATUS_TYPE)))
            .andExpect(jsonPath("$.[*].contractStatusTypeDescription").value(hasItem(DEFAULT_CONTRACT_STATUS_TYPE_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restContractStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContractStatusShouldNotBeFound(String filter) throws Exception {
        restContractStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContractStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContractStatus() throws Exception {
        // Get the contractStatus
        restContractStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContractStatus() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        int databaseSizeBeforeUpdate = contractStatusRepository.findAll().size();

        // Update the contractStatus
        ContractStatus updatedContractStatus = contractStatusRepository.findById(contractStatus.getId()).get();
        // Disconnect from session so that the updates on updatedContractStatus are not directly saved in db
        em.detach(updatedContractStatus);
        updatedContractStatus
            .contractStatusCode(UPDATED_CONTRACT_STATUS_CODE)
            .contractStatusType(UPDATED_CONTRACT_STATUS_TYPE)
            .contractStatusTypeDescription(UPDATED_CONTRACT_STATUS_TYPE_DESCRIPTION);
        ContractStatusDTO contractStatusDTO = contractStatusMapper.toDto(updatedContractStatus);

        restContractStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContractStatus in the database
        List<ContractStatus> contractStatusList = contractStatusRepository.findAll();
        assertThat(contractStatusList).hasSize(databaseSizeBeforeUpdate);
        ContractStatus testContractStatus = contractStatusList.get(contractStatusList.size() - 1);
        assertThat(testContractStatus.getContractStatusCode()).isEqualTo(UPDATED_CONTRACT_STATUS_CODE);
        assertThat(testContractStatus.getContractStatusType()).isEqualTo(UPDATED_CONTRACT_STATUS_TYPE);
        assertThat(testContractStatus.getContractStatusTypeDescription()).isEqualTo(UPDATED_CONTRACT_STATUS_TYPE_DESCRIPTION);

        // Validate the ContractStatus in Elasticsearch
        verify(mockContractStatusSearchRepository).save(testContractStatus);
    }

    @Test
    @Transactional
    void putNonExistingContractStatus() throws Exception {
        int databaseSizeBeforeUpdate = contractStatusRepository.findAll().size();
        contractStatus.setId(count.incrementAndGet());

        // Create the ContractStatus
        ContractStatusDTO contractStatusDTO = contractStatusMapper.toDto(contractStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractStatus in the database
        List<ContractStatus> contractStatusList = contractStatusRepository.findAll();
        assertThat(contractStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContractStatus in Elasticsearch
        verify(mockContractStatusSearchRepository, times(0)).save(contractStatus);
    }

    @Test
    @Transactional
    void putWithIdMismatchContractStatus() throws Exception {
        int databaseSizeBeforeUpdate = contractStatusRepository.findAll().size();
        contractStatus.setId(count.incrementAndGet());

        // Create the ContractStatus
        ContractStatusDTO contractStatusDTO = contractStatusMapper.toDto(contractStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractStatus in the database
        List<ContractStatus> contractStatusList = contractStatusRepository.findAll();
        assertThat(contractStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContractStatus in Elasticsearch
        verify(mockContractStatusSearchRepository, times(0)).save(contractStatus);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContractStatus() throws Exception {
        int databaseSizeBeforeUpdate = contractStatusRepository.findAll().size();
        contractStatus.setId(count.incrementAndGet());

        // Create the ContractStatus
        ContractStatusDTO contractStatusDTO = contractStatusMapper.toDto(contractStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractStatusMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContractStatus in the database
        List<ContractStatus> contractStatusList = contractStatusRepository.findAll();
        assertThat(contractStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContractStatus in Elasticsearch
        verify(mockContractStatusSearchRepository, times(0)).save(contractStatus);
    }

    @Test
    @Transactional
    void partialUpdateContractStatusWithPatch() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        int databaseSizeBeforeUpdate = contractStatusRepository.findAll().size();

        // Update the contractStatus using partial update
        ContractStatus partialUpdatedContractStatus = new ContractStatus();
        partialUpdatedContractStatus.setId(contractStatus.getId());

        partialUpdatedContractStatus
            .contractStatusCode(UPDATED_CONTRACT_STATUS_CODE)
            .contractStatusType(UPDATED_CONTRACT_STATUS_TYPE)
            .contractStatusTypeDescription(UPDATED_CONTRACT_STATUS_TYPE_DESCRIPTION);

        restContractStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContractStatus))
            )
            .andExpect(status().isOk());

        // Validate the ContractStatus in the database
        List<ContractStatus> contractStatusList = contractStatusRepository.findAll();
        assertThat(contractStatusList).hasSize(databaseSizeBeforeUpdate);
        ContractStatus testContractStatus = contractStatusList.get(contractStatusList.size() - 1);
        assertThat(testContractStatus.getContractStatusCode()).isEqualTo(UPDATED_CONTRACT_STATUS_CODE);
        assertThat(testContractStatus.getContractStatusType()).isEqualTo(UPDATED_CONTRACT_STATUS_TYPE);
        assertThat(testContractStatus.getContractStatusTypeDescription()).isEqualTo(UPDATED_CONTRACT_STATUS_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateContractStatusWithPatch() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        int databaseSizeBeforeUpdate = contractStatusRepository.findAll().size();

        // Update the contractStatus using partial update
        ContractStatus partialUpdatedContractStatus = new ContractStatus();
        partialUpdatedContractStatus.setId(contractStatus.getId());

        partialUpdatedContractStatus
            .contractStatusCode(UPDATED_CONTRACT_STATUS_CODE)
            .contractStatusType(UPDATED_CONTRACT_STATUS_TYPE)
            .contractStatusTypeDescription(UPDATED_CONTRACT_STATUS_TYPE_DESCRIPTION);

        restContractStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContractStatus))
            )
            .andExpect(status().isOk());

        // Validate the ContractStatus in the database
        List<ContractStatus> contractStatusList = contractStatusRepository.findAll();
        assertThat(contractStatusList).hasSize(databaseSizeBeforeUpdate);
        ContractStatus testContractStatus = contractStatusList.get(contractStatusList.size() - 1);
        assertThat(testContractStatus.getContractStatusCode()).isEqualTo(UPDATED_CONTRACT_STATUS_CODE);
        assertThat(testContractStatus.getContractStatusType()).isEqualTo(UPDATED_CONTRACT_STATUS_TYPE);
        assertThat(testContractStatus.getContractStatusTypeDescription()).isEqualTo(UPDATED_CONTRACT_STATUS_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingContractStatus() throws Exception {
        int databaseSizeBeforeUpdate = contractStatusRepository.findAll().size();
        contractStatus.setId(count.incrementAndGet());

        // Create the ContractStatus
        ContractStatusDTO contractStatusDTO = contractStatusMapper.toDto(contractStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contractStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractStatus in the database
        List<ContractStatus> contractStatusList = contractStatusRepository.findAll();
        assertThat(contractStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContractStatus in Elasticsearch
        verify(mockContractStatusSearchRepository, times(0)).save(contractStatus);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContractStatus() throws Exception {
        int databaseSizeBeforeUpdate = contractStatusRepository.findAll().size();
        contractStatus.setId(count.incrementAndGet());

        // Create the ContractStatus
        ContractStatusDTO contractStatusDTO = contractStatusMapper.toDto(contractStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractStatus in the database
        List<ContractStatus> contractStatusList = contractStatusRepository.findAll();
        assertThat(contractStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContractStatus in Elasticsearch
        verify(mockContractStatusSearchRepository, times(0)).save(contractStatus);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContractStatus() throws Exception {
        int databaseSizeBeforeUpdate = contractStatusRepository.findAll().size();
        contractStatus.setId(count.incrementAndGet());

        // Create the ContractStatus
        ContractStatusDTO contractStatusDTO = contractStatusMapper.toDto(contractStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContractStatus in the database
        List<ContractStatus> contractStatusList = contractStatusRepository.findAll();
        assertThat(contractStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContractStatus in Elasticsearch
        verify(mockContractStatusSearchRepository, times(0)).save(contractStatus);
    }

    @Test
    @Transactional
    void deleteContractStatus() throws Exception {
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);

        int databaseSizeBeforeDelete = contractStatusRepository.findAll().size();

        // Delete the contractStatus
        restContractStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, contractStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContractStatus> contractStatusList = contractStatusRepository.findAll();
        assertThat(contractStatusList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ContractStatus in Elasticsearch
        verify(mockContractStatusSearchRepository, times(1)).deleteById(contractStatus.getId());
    }

    @Test
    @Transactional
    void searchContractStatus() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        contractStatusRepository.saveAndFlush(contractStatus);
        when(mockContractStatusSearchRepository.search("id:" + contractStatus.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(contractStatus), PageRequest.of(0, 1), 1));

        // Search the contractStatus
        restContractStatusMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + contractStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].contractStatusCode").value(hasItem(DEFAULT_CONTRACT_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].contractStatusType").value(hasItem(DEFAULT_CONTRACT_STATUS_TYPE)))
            .andExpect(jsonPath("$.[*].contractStatusTypeDescription").value(hasItem(DEFAULT_CONTRACT_STATUS_TYPE_DESCRIPTION.toString())));
    }
}
