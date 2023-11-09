package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
import io.github.erp.domain.CrbAgentServiceType;
import io.github.erp.repository.CrbAgentServiceTypeRepository;
import io.github.erp.repository.search.CrbAgentServiceTypeSearchRepository;
import io.github.erp.service.criteria.CrbAgentServiceTypeCriteria;
import io.github.erp.service.dto.CrbAgentServiceTypeDTO;
import io.github.erp.service.mapper.CrbAgentServiceTypeMapper;
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
 * Integration tests for the {@link CrbAgentServiceTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CrbAgentServiceTypeResourceIT {

    private static final String DEFAULT_AGENT_SERVICE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AGENT_SERVICE_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_AGENT_SERVICE_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_AGENT_SERVICE_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crb-agent-service-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/crb-agent-service-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbAgentServiceTypeRepository crbAgentServiceTypeRepository;

    @Autowired
    private CrbAgentServiceTypeMapper crbAgentServiceTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbAgentServiceTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbAgentServiceTypeSearchRepository mockCrbAgentServiceTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbAgentServiceTypeMockMvc;

    private CrbAgentServiceType crbAgentServiceType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbAgentServiceType createEntity(EntityManager em) {
        CrbAgentServiceType crbAgentServiceType = new CrbAgentServiceType()
            .agentServiceTypeCode(DEFAULT_AGENT_SERVICE_TYPE_CODE)
            .agentServiceTypeDetails(DEFAULT_AGENT_SERVICE_TYPE_DETAILS);
        return crbAgentServiceType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbAgentServiceType createUpdatedEntity(EntityManager em) {
        CrbAgentServiceType crbAgentServiceType = new CrbAgentServiceType()
            .agentServiceTypeCode(UPDATED_AGENT_SERVICE_TYPE_CODE)
            .agentServiceTypeDetails(UPDATED_AGENT_SERVICE_TYPE_DETAILS);
        return crbAgentServiceType;
    }

    @BeforeEach
    public void initTest() {
        crbAgentServiceType = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbAgentServiceType() throws Exception {
        int databaseSizeBeforeCreate = crbAgentServiceTypeRepository.findAll().size();
        // Create the CrbAgentServiceType
        CrbAgentServiceTypeDTO crbAgentServiceTypeDTO = crbAgentServiceTypeMapper.toDto(crbAgentServiceType);
        restCrbAgentServiceTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAgentServiceTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbAgentServiceType in the database
        List<CrbAgentServiceType> crbAgentServiceTypeList = crbAgentServiceTypeRepository.findAll();
        assertThat(crbAgentServiceTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CrbAgentServiceType testCrbAgentServiceType = crbAgentServiceTypeList.get(crbAgentServiceTypeList.size() - 1);
        assertThat(testCrbAgentServiceType.getAgentServiceTypeCode()).isEqualTo(DEFAULT_AGENT_SERVICE_TYPE_CODE);
        assertThat(testCrbAgentServiceType.getAgentServiceTypeDetails()).isEqualTo(DEFAULT_AGENT_SERVICE_TYPE_DETAILS);

        // Validate the CrbAgentServiceType in Elasticsearch
        verify(mockCrbAgentServiceTypeSearchRepository, times(1)).save(testCrbAgentServiceType);
    }

    @Test
    @Transactional
    void createCrbAgentServiceTypeWithExistingId() throws Exception {
        // Create the CrbAgentServiceType with an existing ID
        crbAgentServiceType.setId(1L);
        CrbAgentServiceTypeDTO crbAgentServiceTypeDTO = crbAgentServiceTypeMapper.toDto(crbAgentServiceType);

        int databaseSizeBeforeCreate = crbAgentServiceTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbAgentServiceTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAgentServiceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAgentServiceType in the database
        List<CrbAgentServiceType> crbAgentServiceTypeList = crbAgentServiceTypeRepository.findAll();
        assertThat(crbAgentServiceTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbAgentServiceType in Elasticsearch
        verify(mockCrbAgentServiceTypeSearchRepository, times(0)).save(crbAgentServiceType);
    }

    @Test
    @Transactional
    void checkAgentServiceTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbAgentServiceTypeRepository.findAll().size();
        // set the field null
        crbAgentServiceType.setAgentServiceTypeCode(null);

        // Create the CrbAgentServiceType, which fails.
        CrbAgentServiceTypeDTO crbAgentServiceTypeDTO = crbAgentServiceTypeMapper.toDto(crbAgentServiceType);

        restCrbAgentServiceTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAgentServiceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbAgentServiceType> crbAgentServiceTypeList = crbAgentServiceTypeRepository.findAll();
        assertThat(crbAgentServiceTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbAgentServiceTypes() throws Exception {
        // Initialize the database
        crbAgentServiceTypeRepository.saveAndFlush(crbAgentServiceType);

        // Get all the crbAgentServiceTypeList
        restCrbAgentServiceTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbAgentServiceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].agentServiceTypeCode").value(hasItem(DEFAULT_AGENT_SERVICE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].agentServiceTypeDetails").value(hasItem(DEFAULT_AGENT_SERVICE_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getCrbAgentServiceType() throws Exception {
        // Initialize the database
        crbAgentServiceTypeRepository.saveAndFlush(crbAgentServiceType);

        // Get the crbAgentServiceType
        restCrbAgentServiceTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, crbAgentServiceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbAgentServiceType.getId().intValue()))
            .andExpect(jsonPath("$.agentServiceTypeCode").value(DEFAULT_AGENT_SERVICE_TYPE_CODE))
            .andExpect(jsonPath("$.agentServiceTypeDetails").value(DEFAULT_AGENT_SERVICE_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getCrbAgentServiceTypesByIdFiltering() throws Exception {
        // Initialize the database
        crbAgentServiceTypeRepository.saveAndFlush(crbAgentServiceType);

        Long id = crbAgentServiceType.getId();

        defaultCrbAgentServiceTypeShouldBeFound("id.equals=" + id);
        defaultCrbAgentServiceTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCrbAgentServiceTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbAgentServiceTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbAgentServiceTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbAgentServiceTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbAgentServiceTypesByAgentServiceTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbAgentServiceTypeRepository.saveAndFlush(crbAgentServiceType);

        // Get all the crbAgentServiceTypeList where agentServiceTypeCode equals to DEFAULT_AGENT_SERVICE_TYPE_CODE
        defaultCrbAgentServiceTypeShouldBeFound("agentServiceTypeCode.equals=" + DEFAULT_AGENT_SERVICE_TYPE_CODE);

        // Get all the crbAgentServiceTypeList where agentServiceTypeCode equals to UPDATED_AGENT_SERVICE_TYPE_CODE
        defaultCrbAgentServiceTypeShouldNotBeFound("agentServiceTypeCode.equals=" + UPDATED_AGENT_SERVICE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAgentServiceTypesByAgentServiceTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbAgentServiceTypeRepository.saveAndFlush(crbAgentServiceType);

        // Get all the crbAgentServiceTypeList where agentServiceTypeCode not equals to DEFAULT_AGENT_SERVICE_TYPE_CODE
        defaultCrbAgentServiceTypeShouldNotBeFound("agentServiceTypeCode.notEquals=" + DEFAULT_AGENT_SERVICE_TYPE_CODE);

        // Get all the crbAgentServiceTypeList where agentServiceTypeCode not equals to UPDATED_AGENT_SERVICE_TYPE_CODE
        defaultCrbAgentServiceTypeShouldBeFound("agentServiceTypeCode.notEquals=" + UPDATED_AGENT_SERVICE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAgentServiceTypesByAgentServiceTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbAgentServiceTypeRepository.saveAndFlush(crbAgentServiceType);

        // Get all the crbAgentServiceTypeList where agentServiceTypeCode in DEFAULT_AGENT_SERVICE_TYPE_CODE or UPDATED_AGENT_SERVICE_TYPE_CODE
        defaultCrbAgentServiceTypeShouldBeFound(
            "agentServiceTypeCode.in=" + DEFAULT_AGENT_SERVICE_TYPE_CODE + "," + UPDATED_AGENT_SERVICE_TYPE_CODE
        );

        // Get all the crbAgentServiceTypeList where agentServiceTypeCode equals to UPDATED_AGENT_SERVICE_TYPE_CODE
        defaultCrbAgentServiceTypeShouldNotBeFound("agentServiceTypeCode.in=" + UPDATED_AGENT_SERVICE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAgentServiceTypesByAgentServiceTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbAgentServiceTypeRepository.saveAndFlush(crbAgentServiceType);

        // Get all the crbAgentServiceTypeList where agentServiceTypeCode is not null
        defaultCrbAgentServiceTypeShouldBeFound("agentServiceTypeCode.specified=true");

        // Get all the crbAgentServiceTypeList where agentServiceTypeCode is null
        defaultCrbAgentServiceTypeShouldNotBeFound("agentServiceTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbAgentServiceTypesByAgentServiceTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        crbAgentServiceTypeRepository.saveAndFlush(crbAgentServiceType);

        // Get all the crbAgentServiceTypeList where agentServiceTypeCode contains DEFAULT_AGENT_SERVICE_TYPE_CODE
        defaultCrbAgentServiceTypeShouldBeFound("agentServiceTypeCode.contains=" + DEFAULT_AGENT_SERVICE_TYPE_CODE);

        // Get all the crbAgentServiceTypeList where agentServiceTypeCode contains UPDATED_AGENT_SERVICE_TYPE_CODE
        defaultCrbAgentServiceTypeShouldNotBeFound("agentServiceTypeCode.contains=" + UPDATED_AGENT_SERVICE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAgentServiceTypesByAgentServiceTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbAgentServiceTypeRepository.saveAndFlush(crbAgentServiceType);

        // Get all the crbAgentServiceTypeList where agentServiceTypeCode does not contain DEFAULT_AGENT_SERVICE_TYPE_CODE
        defaultCrbAgentServiceTypeShouldNotBeFound("agentServiceTypeCode.doesNotContain=" + DEFAULT_AGENT_SERVICE_TYPE_CODE);

        // Get all the crbAgentServiceTypeList where agentServiceTypeCode does not contain UPDATED_AGENT_SERVICE_TYPE_CODE
        defaultCrbAgentServiceTypeShouldBeFound("agentServiceTypeCode.doesNotContain=" + UPDATED_AGENT_SERVICE_TYPE_CODE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbAgentServiceTypeShouldBeFound(String filter) throws Exception {
        restCrbAgentServiceTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbAgentServiceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].agentServiceTypeCode").value(hasItem(DEFAULT_AGENT_SERVICE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].agentServiceTypeDetails").value(hasItem(DEFAULT_AGENT_SERVICE_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restCrbAgentServiceTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbAgentServiceTypeShouldNotBeFound(String filter) throws Exception {
        restCrbAgentServiceTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbAgentServiceTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbAgentServiceType() throws Exception {
        // Get the crbAgentServiceType
        restCrbAgentServiceTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbAgentServiceType() throws Exception {
        // Initialize the database
        crbAgentServiceTypeRepository.saveAndFlush(crbAgentServiceType);

        int databaseSizeBeforeUpdate = crbAgentServiceTypeRepository.findAll().size();

        // Update the crbAgentServiceType
        CrbAgentServiceType updatedCrbAgentServiceType = crbAgentServiceTypeRepository.findById(crbAgentServiceType.getId()).get();
        // Disconnect from session so that the updates on updatedCrbAgentServiceType are not directly saved in db
        em.detach(updatedCrbAgentServiceType);
        updatedCrbAgentServiceType
            .agentServiceTypeCode(UPDATED_AGENT_SERVICE_TYPE_CODE)
            .agentServiceTypeDetails(UPDATED_AGENT_SERVICE_TYPE_DETAILS);
        CrbAgentServiceTypeDTO crbAgentServiceTypeDTO = crbAgentServiceTypeMapper.toDto(updatedCrbAgentServiceType);

        restCrbAgentServiceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbAgentServiceTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAgentServiceTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbAgentServiceType in the database
        List<CrbAgentServiceType> crbAgentServiceTypeList = crbAgentServiceTypeRepository.findAll();
        assertThat(crbAgentServiceTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbAgentServiceType testCrbAgentServiceType = crbAgentServiceTypeList.get(crbAgentServiceTypeList.size() - 1);
        assertThat(testCrbAgentServiceType.getAgentServiceTypeCode()).isEqualTo(UPDATED_AGENT_SERVICE_TYPE_CODE);
        assertThat(testCrbAgentServiceType.getAgentServiceTypeDetails()).isEqualTo(UPDATED_AGENT_SERVICE_TYPE_DETAILS);

        // Validate the CrbAgentServiceType in Elasticsearch
        verify(mockCrbAgentServiceTypeSearchRepository).save(testCrbAgentServiceType);
    }

    @Test
    @Transactional
    void putNonExistingCrbAgentServiceType() throws Exception {
        int databaseSizeBeforeUpdate = crbAgentServiceTypeRepository.findAll().size();
        crbAgentServiceType.setId(count.incrementAndGet());

        // Create the CrbAgentServiceType
        CrbAgentServiceTypeDTO crbAgentServiceTypeDTO = crbAgentServiceTypeMapper.toDto(crbAgentServiceType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbAgentServiceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbAgentServiceTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAgentServiceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAgentServiceType in the database
        List<CrbAgentServiceType> crbAgentServiceTypeList = crbAgentServiceTypeRepository.findAll();
        assertThat(crbAgentServiceTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAgentServiceType in Elasticsearch
        verify(mockCrbAgentServiceTypeSearchRepository, times(0)).save(crbAgentServiceType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbAgentServiceType() throws Exception {
        int databaseSizeBeforeUpdate = crbAgentServiceTypeRepository.findAll().size();
        crbAgentServiceType.setId(count.incrementAndGet());

        // Create the CrbAgentServiceType
        CrbAgentServiceTypeDTO crbAgentServiceTypeDTO = crbAgentServiceTypeMapper.toDto(crbAgentServiceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAgentServiceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAgentServiceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAgentServiceType in the database
        List<CrbAgentServiceType> crbAgentServiceTypeList = crbAgentServiceTypeRepository.findAll();
        assertThat(crbAgentServiceTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAgentServiceType in Elasticsearch
        verify(mockCrbAgentServiceTypeSearchRepository, times(0)).save(crbAgentServiceType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbAgentServiceType() throws Exception {
        int databaseSizeBeforeUpdate = crbAgentServiceTypeRepository.findAll().size();
        crbAgentServiceType.setId(count.incrementAndGet());

        // Create the CrbAgentServiceType
        CrbAgentServiceTypeDTO crbAgentServiceTypeDTO = crbAgentServiceTypeMapper.toDto(crbAgentServiceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAgentServiceTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAgentServiceTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbAgentServiceType in the database
        List<CrbAgentServiceType> crbAgentServiceTypeList = crbAgentServiceTypeRepository.findAll();
        assertThat(crbAgentServiceTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAgentServiceType in Elasticsearch
        verify(mockCrbAgentServiceTypeSearchRepository, times(0)).save(crbAgentServiceType);
    }

    @Test
    @Transactional
    void partialUpdateCrbAgentServiceTypeWithPatch() throws Exception {
        // Initialize the database
        crbAgentServiceTypeRepository.saveAndFlush(crbAgentServiceType);

        int databaseSizeBeforeUpdate = crbAgentServiceTypeRepository.findAll().size();

        // Update the crbAgentServiceType using partial update
        CrbAgentServiceType partialUpdatedCrbAgentServiceType = new CrbAgentServiceType();
        partialUpdatedCrbAgentServiceType.setId(crbAgentServiceType.getId());

        partialUpdatedCrbAgentServiceType
            .agentServiceTypeCode(UPDATED_AGENT_SERVICE_TYPE_CODE)
            .agentServiceTypeDetails(UPDATED_AGENT_SERVICE_TYPE_DETAILS);

        restCrbAgentServiceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbAgentServiceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbAgentServiceType))
            )
            .andExpect(status().isOk());

        // Validate the CrbAgentServiceType in the database
        List<CrbAgentServiceType> crbAgentServiceTypeList = crbAgentServiceTypeRepository.findAll();
        assertThat(crbAgentServiceTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbAgentServiceType testCrbAgentServiceType = crbAgentServiceTypeList.get(crbAgentServiceTypeList.size() - 1);
        assertThat(testCrbAgentServiceType.getAgentServiceTypeCode()).isEqualTo(UPDATED_AGENT_SERVICE_TYPE_CODE);
        assertThat(testCrbAgentServiceType.getAgentServiceTypeDetails()).isEqualTo(UPDATED_AGENT_SERVICE_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCrbAgentServiceTypeWithPatch() throws Exception {
        // Initialize the database
        crbAgentServiceTypeRepository.saveAndFlush(crbAgentServiceType);

        int databaseSizeBeforeUpdate = crbAgentServiceTypeRepository.findAll().size();

        // Update the crbAgentServiceType using partial update
        CrbAgentServiceType partialUpdatedCrbAgentServiceType = new CrbAgentServiceType();
        partialUpdatedCrbAgentServiceType.setId(crbAgentServiceType.getId());

        partialUpdatedCrbAgentServiceType
            .agentServiceTypeCode(UPDATED_AGENT_SERVICE_TYPE_CODE)
            .agentServiceTypeDetails(UPDATED_AGENT_SERVICE_TYPE_DETAILS);

        restCrbAgentServiceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbAgentServiceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbAgentServiceType))
            )
            .andExpect(status().isOk());

        // Validate the CrbAgentServiceType in the database
        List<CrbAgentServiceType> crbAgentServiceTypeList = crbAgentServiceTypeRepository.findAll();
        assertThat(crbAgentServiceTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbAgentServiceType testCrbAgentServiceType = crbAgentServiceTypeList.get(crbAgentServiceTypeList.size() - 1);
        assertThat(testCrbAgentServiceType.getAgentServiceTypeCode()).isEqualTo(UPDATED_AGENT_SERVICE_TYPE_CODE);
        assertThat(testCrbAgentServiceType.getAgentServiceTypeDetails()).isEqualTo(UPDATED_AGENT_SERVICE_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCrbAgentServiceType() throws Exception {
        int databaseSizeBeforeUpdate = crbAgentServiceTypeRepository.findAll().size();
        crbAgentServiceType.setId(count.incrementAndGet());

        // Create the CrbAgentServiceType
        CrbAgentServiceTypeDTO crbAgentServiceTypeDTO = crbAgentServiceTypeMapper.toDto(crbAgentServiceType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbAgentServiceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbAgentServiceTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbAgentServiceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAgentServiceType in the database
        List<CrbAgentServiceType> crbAgentServiceTypeList = crbAgentServiceTypeRepository.findAll();
        assertThat(crbAgentServiceTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAgentServiceType in Elasticsearch
        verify(mockCrbAgentServiceTypeSearchRepository, times(0)).save(crbAgentServiceType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbAgentServiceType() throws Exception {
        int databaseSizeBeforeUpdate = crbAgentServiceTypeRepository.findAll().size();
        crbAgentServiceType.setId(count.incrementAndGet());

        // Create the CrbAgentServiceType
        CrbAgentServiceTypeDTO crbAgentServiceTypeDTO = crbAgentServiceTypeMapper.toDto(crbAgentServiceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAgentServiceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbAgentServiceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAgentServiceType in the database
        List<CrbAgentServiceType> crbAgentServiceTypeList = crbAgentServiceTypeRepository.findAll();
        assertThat(crbAgentServiceTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAgentServiceType in Elasticsearch
        verify(mockCrbAgentServiceTypeSearchRepository, times(0)).save(crbAgentServiceType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbAgentServiceType() throws Exception {
        int databaseSizeBeforeUpdate = crbAgentServiceTypeRepository.findAll().size();
        crbAgentServiceType.setId(count.incrementAndGet());

        // Create the CrbAgentServiceType
        CrbAgentServiceTypeDTO crbAgentServiceTypeDTO = crbAgentServiceTypeMapper.toDto(crbAgentServiceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAgentServiceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbAgentServiceTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbAgentServiceType in the database
        List<CrbAgentServiceType> crbAgentServiceTypeList = crbAgentServiceTypeRepository.findAll();
        assertThat(crbAgentServiceTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAgentServiceType in Elasticsearch
        verify(mockCrbAgentServiceTypeSearchRepository, times(0)).save(crbAgentServiceType);
    }

    @Test
    @Transactional
    void deleteCrbAgentServiceType() throws Exception {
        // Initialize the database
        crbAgentServiceTypeRepository.saveAndFlush(crbAgentServiceType);

        int databaseSizeBeforeDelete = crbAgentServiceTypeRepository.findAll().size();

        // Delete the crbAgentServiceType
        restCrbAgentServiceTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbAgentServiceType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbAgentServiceType> crbAgentServiceTypeList = crbAgentServiceTypeRepository.findAll();
        assertThat(crbAgentServiceTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbAgentServiceType in Elasticsearch
        verify(mockCrbAgentServiceTypeSearchRepository, times(1)).deleteById(crbAgentServiceType.getId());
    }

    @Test
    @Transactional
    void searchCrbAgentServiceType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbAgentServiceTypeRepository.saveAndFlush(crbAgentServiceType);
        when(mockCrbAgentServiceTypeSearchRepository.search("id:" + crbAgentServiceType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbAgentServiceType), PageRequest.of(0, 1), 1));

        // Search the crbAgentServiceType
        restCrbAgentServiceTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbAgentServiceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbAgentServiceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].agentServiceTypeCode").value(hasItem(DEFAULT_AGENT_SERVICE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].agentServiceTypeDetails").value(hasItem(DEFAULT_AGENT_SERVICE_TYPE_DETAILS.toString())));
    }
}
