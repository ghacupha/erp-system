package io.github.erp.web.rest;

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
import io.github.erp.domain.CrbComplaintType;
import io.github.erp.repository.CrbComplaintTypeRepository;
import io.github.erp.repository.search.CrbComplaintTypeSearchRepository;
import io.github.erp.service.criteria.CrbComplaintTypeCriteria;
import io.github.erp.service.dto.CrbComplaintTypeDTO;
import io.github.erp.service.mapper.CrbComplaintTypeMapper;
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
 * Integration tests for the {@link CrbComplaintTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CrbComplaintTypeResourceIT {

    private static final String DEFAULT_COMPLAINT_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COMPLAINT_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPLAINT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_COMPLAINT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPLAINT_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_COMPLAINT_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crb-complaint-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/crb-complaint-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbComplaintTypeRepository crbComplaintTypeRepository;

    @Autowired
    private CrbComplaintTypeMapper crbComplaintTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbComplaintTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbComplaintTypeSearchRepository mockCrbComplaintTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbComplaintTypeMockMvc;

    private CrbComplaintType crbComplaintType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbComplaintType createEntity(EntityManager em) {
        CrbComplaintType crbComplaintType = new CrbComplaintType()
            .complaintTypeCode(DEFAULT_COMPLAINT_TYPE_CODE)
            .complaintType(DEFAULT_COMPLAINT_TYPE)
            .complaintTypeDetails(DEFAULT_COMPLAINT_TYPE_DETAILS);
        return crbComplaintType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbComplaintType createUpdatedEntity(EntityManager em) {
        CrbComplaintType crbComplaintType = new CrbComplaintType()
            .complaintTypeCode(UPDATED_COMPLAINT_TYPE_CODE)
            .complaintType(UPDATED_COMPLAINT_TYPE)
            .complaintTypeDetails(UPDATED_COMPLAINT_TYPE_DETAILS);
        return crbComplaintType;
    }

    @BeforeEach
    public void initTest() {
        crbComplaintType = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbComplaintType() throws Exception {
        int databaseSizeBeforeCreate = crbComplaintTypeRepository.findAll().size();
        // Create the CrbComplaintType
        CrbComplaintTypeDTO crbComplaintTypeDTO = crbComplaintTypeMapper.toDto(crbComplaintType);
        restCrbComplaintTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbComplaintTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbComplaintType in the database
        List<CrbComplaintType> crbComplaintTypeList = crbComplaintTypeRepository.findAll();
        assertThat(crbComplaintTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CrbComplaintType testCrbComplaintType = crbComplaintTypeList.get(crbComplaintTypeList.size() - 1);
        assertThat(testCrbComplaintType.getComplaintTypeCode()).isEqualTo(DEFAULT_COMPLAINT_TYPE_CODE);
        assertThat(testCrbComplaintType.getComplaintType()).isEqualTo(DEFAULT_COMPLAINT_TYPE);
        assertThat(testCrbComplaintType.getComplaintTypeDetails()).isEqualTo(DEFAULT_COMPLAINT_TYPE_DETAILS);

        // Validate the CrbComplaintType in Elasticsearch
        verify(mockCrbComplaintTypeSearchRepository, times(1)).save(testCrbComplaintType);
    }

    @Test
    @Transactional
    void createCrbComplaintTypeWithExistingId() throws Exception {
        // Create the CrbComplaintType with an existing ID
        crbComplaintType.setId(1L);
        CrbComplaintTypeDTO crbComplaintTypeDTO = crbComplaintTypeMapper.toDto(crbComplaintType);

        int databaseSizeBeforeCreate = crbComplaintTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbComplaintTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbComplaintTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbComplaintType in the database
        List<CrbComplaintType> crbComplaintTypeList = crbComplaintTypeRepository.findAll();
        assertThat(crbComplaintTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbComplaintType in Elasticsearch
        verify(mockCrbComplaintTypeSearchRepository, times(0)).save(crbComplaintType);
    }

    @Test
    @Transactional
    void checkComplaintTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbComplaintTypeRepository.findAll().size();
        // set the field null
        crbComplaintType.setComplaintTypeCode(null);

        // Create the CrbComplaintType, which fails.
        CrbComplaintTypeDTO crbComplaintTypeDTO = crbComplaintTypeMapper.toDto(crbComplaintType);

        restCrbComplaintTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbComplaintTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbComplaintType> crbComplaintTypeList = crbComplaintTypeRepository.findAll();
        assertThat(crbComplaintTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkComplaintTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbComplaintTypeRepository.findAll().size();
        // set the field null
        crbComplaintType.setComplaintType(null);

        // Create the CrbComplaintType, which fails.
        CrbComplaintTypeDTO crbComplaintTypeDTO = crbComplaintTypeMapper.toDto(crbComplaintType);

        restCrbComplaintTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbComplaintTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbComplaintType> crbComplaintTypeList = crbComplaintTypeRepository.findAll();
        assertThat(crbComplaintTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbComplaintTypes() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        // Get all the crbComplaintTypeList
        restCrbComplaintTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbComplaintType.getId().intValue())))
            .andExpect(jsonPath("$.[*].complaintTypeCode").value(hasItem(DEFAULT_COMPLAINT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].complaintType").value(hasItem(DEFAULT_COMPLAINT_TYPE)))
            .andExpect(jsonPath("$.[*].complaintTypeDetails").value(hasItem(DEFAULT_COMPLAINT_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getCrbComplaintType() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        // Get the crbComplaintType
        restCrbComplaintTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, crbComplaintType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbComplaintType.getId().intValue()))
            .andExpect(jsonPath("$.complaintTypeCode").value(DEFAULT_COMPLAINT_TYPE_CODE))
            .andExpect(jsonPath("$.complaintType").value(DEFAULT_COMPLAINT_TYPE))
            .andExpect(jsonPath("$.complaintTypeDetails").value(DEFAULT_COMPLAINT_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getCrbComplaintTypesByIdFiltering() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        Long id = crbComplaintType.getId();

        defaultCrbComplaintTypeShouldBeFound("id.equals=" + id);
        defaultCrbComplaintTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCrbComplaintTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbComplaintTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbComplaintTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbComplaintTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbComplaintTypesByComplaintTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        // Get all the crbComplaintTypeList where complaintTypeCode equals to DEFAULT_COMPLAINT_TYPE_CODE
        defaultCrbComplaintTypeShouldBeFound("complaintTypeCode.equals=" + DEFAULT_COMPLAINT_TYPE_CODE);

        // Get all the crbComplaintTypeList where complaintTypeCode equals to UPDATED_COMPLAINT_TYPE_CODE
        defaultCrbComplaintTypeShouldNotBeFound("complaintTypeCode.equals=" + UPDATED_COMPLAINT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbComplaintTypesByComplaintTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        // Get all the crbComplaintTypeList where complaintTypeCode not equals to DEFAULT_COMPLAINT_TYPE_CODE
        defaultCrbComplaintTypeShouldNotBeFound("complaintTypeCode.notEquals=" + DEFAULT_COMPLAINT_TYPE_CODE);

        // Get all the crbComplaintTypeList where complaintTypeCode not equals to UPDATED_COMPLAINT_TYPE_CODE
        defaultCrbComplaintTypeShouldBeFound("complaintTypeCode.notEquals=" + UPDATED_COMPLAINT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbComplaintTypesByComplaintTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        // Get all the crbComplaintTypeList where complaintTypeCode in DEFAULT_COMPLAINT_TYPE_CODE or UPDATED_COMPLAINT_TYPE_CODE
        defaultCrbComplaintTypeShouldBeFound("complaintTypeCode.in=" + DEFAULT_COMPLAINT_TYPE_CODE + "," + UPDATED_COMPLAINT_TYPE_CODE);

        // Get all the crbComplaintTypeList where complaintTypeCode equals to UPDATED_COMPLAINT_TYPE_CODE
        defaultCrbComplaintTypeShouldNotBeFound("complaintTypeCode.in=" + UPDATED_COMPLAINT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbComplaintTypesByComplaintTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        // Get all the crbComplaintTypeList where complaintTypeCode is not null
        defaultCrbComplaintTypeShouldBeFound("complaintTypeCode.specified=true");

        // Get all the crbComplaintTypeList where complaintTypeCode is null
        defaultCrbComplaintTypeShouldNotBeFound("complaintTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbComplaintTypesByComplaintTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        // Get all the crbComplaintTypeList where complaintTypeCode contains DEFAULT_COMPLAINT_TYPE_CODE
        defaultCrbComplaintTypeShouldBeFound("complaintTypeCode.contains=" + DEFAULT_COMPLAINT_TYPE_CODE);

        // Get all the crbComplaintTypeList where complaintTypeCode contains UPDATED_COMPLAINT_TYPE_CODE
        defaultCrbComplaintTypeShouldNotBeFound("complaintTypeCode.contains=" + UPDATED_COMPLAINT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbComplaintTypesByComplaintTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        // Get all the crbComplaintTypeList where complaintTypeCode does not contain DEFAULT_COMPLAINT_TYPE_CODE
        defaultCrbComplaintTypeShouldNotBeFound("complaintTypeCode.doesNotContain=" + DEFAULT_COMPLAINT_TYPE_CODE);

        // Get all the crbComplaintTypeList where complaintTypeCode does not contain UPDATED_COMPLAINT_TYPE_CODE
        defaultCrbComplaintTypeShouldBeFound("complaintTypeCode.doesNotContain=" + UPDATED_COMPLAINT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbComplaintTypesByComplaintTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        // Get all the crbComplaintTypeList where complaintType equals to DEFAULT_COMPLAINT_TYPE
        defaultCrbComplaintTypeShouldBeFound("complaintType.equals=" + DEFAULT_COMPLAINT_TYPE);

        // Get all the crbComplaintTypeList where complaintType equals to UPDATED_COMPLAINT_TYPE
        defaultCrbComplaintTypeShouldNotBeFound("complaintType.equals=" + UPDATED_COMPLAINT_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbComplaintTypesByComplaintTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        // Get all the crbComplaintTypeList where complaintType not equals to DEFAULT_COMPLAINT_TYPE
        defaultCrbComplaintTypeShouldNotBeFound("complaintType.notEquals=" + DEFAULT_COMPLAINT_TYPE);

        // Get all the crbComplaintTypeList where complaintType not equals to UPDATED_COMPLAINT_TYPE
        defaultCrbComplaintTypeShouldBeFound("complaintType.notEquals=" + UPDATED_COMPLAINT_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbComplaintTypesByComplaintTypeIsInShouldWork() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        // Get all the crbComplaintTypeList where complaintType in DEFAULT_COMPLAINT_TYPE or UPDATED_COMPLAINT_TYPE
        defaultCrbComplaintTypeShouldBeFound("complaintType.in=" + DEFAULT_COMPLAINT_TYPE + "," + UPDATED_COMPLAINT_TYPE);

        // Get all the crbComplaintTypeList where complaintType equals to UPDATED_COMPLAINT_TYPE
        defaultCrbComplaintTypeShouldNotBeFound("complaintType.in=" + UPDATED_COMPLAINT_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbComplaintTypesByComplaintTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        // Get all the crbComplaintTypeList where complaintType is not null
        defaultCrbComplaintTypeShouldBeFound("complaintType.specified=true");

        // Get all the crbComplaintTypeList where complaintType is null
        defaultCrbComplaintTypeShouldNotBeFound("complaintType.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbComplaintTypesByComplaintTypeContainsSomething() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        // Get all the crbComplaintTypeList where complaintType contains DEFAULT_COMPLAINT_TYPE
        defaultCrbComplaintTypeShouldBeFound("complaintType.contains=" + DEFAULT_COMPLAINT_TYPE);

        // Get all the crbComplaintTypeList where complaintType contains UPDATED_COMPLAINT_TYPE
        defaultCrbComplaintTypeShouldNotBeFound("complaintType.contains=" + UPDATED_COMPLAINT_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbComplaintTypesByComplaintTypeNotContainsSomething() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        // Get all the crbComplaintTypeList where complaintType does not contain DEFAULT_COMPLAINT_TYPE
        defaultCrbComplaintTypeShouldNotBeFound("complaintType.doesNotContain=" + DEFAULT_COMPLAINT_TYPE);

        // Get all the crbComplaintTypeList where complaintType does not contain UPDATED_COMPLAINT_TYPE
        defaultCrbComplaintTypeShouldBeFound("complaintType.doesNotContain=" + UPDATED_COMPLAINT_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbComplaintTypeShouldBeFound(String filter) throws Exception {
        restCrbComplaintTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbComplaintType.getId().intValue())))
            .andExpect(jsonPath("$.[*].complaintTypeCode").value(hasItem(DEFAULT_COMPLAINT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].complaintType").value(hasItem(DEFAULT_COMPLAINT_TYPE)))
            .andExpect(jsonPath("$.[*].complaintTypeDetails").value(hasItem(DEFAULT_COMPLAINT_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restCrbComplaintTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbComplaintTypeShouldNotBeFound(String filter) throws Exception {
        restCrbComplaintTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbComplaintTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbComplaintType() throws Exception {
        // Get the crbComplaintType
        restCrbComplaintTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbComplaintType() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        int databaseSizeBeforeUpdate = crbComplaintTypeRepository.findAll().size();

        // Update the crbComplaintType
        CrbComplaintType updatedCrbComplaintType = crbComplaintTypeRepository.findById(crbComplaintType.getId()).get();
        // Disconnect from session so that the updates on updatedCrbComplaintType are not directly saved in db
        em.detach(updatedCrbComplaintType);
        updatedCrbComplaintType
            .complaintTypeCode(UPDATED_COMPLAINT_TYPE_CODE)
            .complaintType(UPDATED_COMPLAINT_TYPE)
            .complaintTypeDetails(UPDATED_COMPLAINT_TYPE_DETAILS);
        CrbComplaintTypeDTO crbComplaintTypeDTO = crbComplaintTypeMapper.toDto(updatedCrbComplaintType);

        restCrbComplaintTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbComplaintTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbComplaintTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbComplaintType in the database
        List<CrbComplaintType> crbComplaintTypeList = crbComplaintTypeRepository.findAll();
        assertThat(crbComplaintTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbComplaintType testCrbComplaintType = crbComplaintTypeList.get(crbComplaintTypeList.size() - 1);
        assertThat(testCrbComplaintType.getComplaintTypeCode()).isEqualTo(UPDATED_COMPLAINT_TYPE_CODE);
        assertThat(testCrbComplaintType.getComplaintType()).isEqualTo(UPDATED_COMPLAINT_TYPE);
        assertThat(testCrbComplaintType.getComplaintTypeDetails()).isEqualTo(UPDATED_COMPLAINT_TYPE_DETAILS);

        // Validate the CrbComplaintType in Elasticsearch
        verify(mockCrbComplaintTypeSearchRepository).save(testCrbComplaintType);
    }

    @Test
    @Transactional
    void putNonExistingCrbComplaintType() throws Exception {
        int databaseSizeBeforeUpdate = crbComplaintTypeRepository.findAll().size();
        crbComplaintType.setId(count.incrementAndGet());

        // Create the CrbComplaintType
        CrbComplaintTypeDTO crbComplaintTypeDTO = crbComplaintTypeMapper.toDto(crbComplaintType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbComplaintTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbComplaintTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbComplaintTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbComplaintType in the database
        List<CrbComplaintType> crbComplaintTypeList = crbComplaintTypeRepository.findAll();
        assertThat(crbComplaintTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbComplaintType in Elasticsearch
        verify(mockCrbComplaintTypeSearchRepository, times(0)).save(crbComplaintType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbComplaintType() throws Exception {
        int databaseSizeBeforeUpdate = crbComplaintTypeRepository.findAll().size();
        crbComplaintType.setId(count.incrementAndGet());

        // Create the CrbComplaintType
        CrbComplaintTypeDTO crbComplaintTypeDTO = crbComplaintTypeMapper.toDto(crbComplaintType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbComplaintTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbComplaintTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbComplaintType in the database
        List<CrbComplaintType> crbComplaintTypeList = crbComplaintTypeRepository.findAll();
        assertThat(crbComplaintTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbComplaintType in Elasticsearch
        verify(mockCrbComplaintTypeSearchRepository, times(0)).save(crbComplaintType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbComplaintType() throws Exception {
        int databaseSizeBeforeUpdate = crbComplaintTypeRepository.findAll().size();
        crbComplaintType.setId(count.incrementAndGet());

        // Create the CrbComplaintType
        CrbComplaintTypeDTO crbComplaintTypeDTO = crbComplaintTypeMapper.toDto(crbComplaintType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbComplaintTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbComplaintTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbComplaintType in the database
        List<CrbComplaintType> crbComplaintTypeList = crbComplaintTypeRepository.findAll();
        assertThat(crbComplaintTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbComplaintType in Elasticsearch
        verify(mockCrbComplaintTypeSearchRepository, times(0)).save(crbComplaintType);
    }

    @Test
    @Transactional
    void partialUpdateCrbComplaintTypeWithPatch() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        int databaseSizeBeforeUpdate = crbComplaintTypeRepository.findAll().size();

        // Update the crbComplaintType using partial update
        CrbComplaintType partialUpdatedCrbComplaintType = new CrbComplaintType();
        partialUpdatedCrbComplaintType.setId(crbComplaintType.getId());

        partialUpdatedCrbComplaintType.complaintType(UPDATED_COMPLAINT_TYPE).complaintTypeDetails(UPDATED_COMPLAINT_TYPE_DETAILS);

        restCrbComplaintTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbComplaintType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbComplaintType))
            )
            .andExpect(status().isOk());

        // Validate the CrbComplaintType in the database
        List<CrbComplaintType> crbComplaintTypeList = crbComplaintTypeRepository.findAll();
        assertThat(crbComplaintTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbComplaintType testCrbComplaintType = crbComplaintTypeList.get(crbComplaintTypeList.size() - 1);
        assertThat(testCrbComplaintType.getComplaintTypeCode()).isEqualTo(DEFAULT_COMPLAINT_TYPE_CODE);
        assertThat(testCrbComplaintType.getComplaintType()).isEqualTo(UPDATED_COMPLAINT_TYPE);
        assertThat(testCrbComplaintType.getComplaintTypeDetails()).isEqualTo(UPDATED_COMPLAINT_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCrbComplaintTypeWithPatch() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        int databaseSizeBeforeUpdate = crbComplaintTypeRepository.findAll().size();

        // Update the crbComplaintType using partial update
        CrbComplaintType partialUpdatedCrbComplaintType = new CrbComplaintType();
        partialUpdatedCrbComplaintType.setId(crbComplaintType.getId());

        partialUpdatedCrbComplaintType
            .complaintTypeCode(UPDATED_COMPLAINT_TYPE_CODE)
            .complaintType(UPDATED_COMPLAINT_TYPE)
            .complaintTypeDetails(UPDATED_COMPLAINT_TYPE_DETAILS);

        restCrbComplaintTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbComplaintType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbComplaintType))
            )
            .andExpect(status().isOk());

        // Validate the CrbComplaintType in the database
        List<CrbComplaintType> crbComplaintTypeList = crbComplaintTypeRepository.findAll();
        assertThat(crbComplaintTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbComplaintType testCrbComplaintType = crbComplaintTypeList.get(crbComplaintTypeList.size() - 1);
        assertThat(testCrbComplaintType.getComplaintTypeCode()).isEqualTo(UPDATED_COMPLAINT_TYPE_CODE);
        assertThat(testCrbComplaintType.getComplaintType()).isEqualTo(UPDATED_COMPLAINT_TYPE);
        assertThat(testCrbComplaintType.getComplaintTypeDetails()).isEqualTo(UPDATED_COMPLAINT_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCrbComplaintType() throws Exception {
        int databaseSizeBeforeUpdate = crbComplaintTypeRepository.findAll().size();
        crbComplaintType.setId(count.incrementAndGet());

        // Create the CrbComplaintType
        CrbComplaintTypeDTO crbComplaintTypeDTO = crbComplaintTypeMapper.toDto(crbComplaintType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbComplaintTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbComplaintTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbComplaintTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbComplaintType in the database
        List<CrbComplaintType> crbComplaintTypeList = crbComplaintTypeRepository.findAll();
        assertThat(crbComplaintTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbComplaintType in Elasticsearch
        verify(mockCrbComplaintTypeSearchRepository, times(0)).save(crbComplaintType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbComplaintType() throws Exception {
        int databaseSizeBeforeUpdate = crbComplaintTypeRepository.findAll().size();
        crbComplaintType.setId(count.incrementAndGet());

        // Create the CrbComplaintType
        CrbComplaintTypeDTO crbComplaintTypeDTO = crbComplaintTypeMapper.toDto(crbComplaintType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbComplaintTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbComplaintTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbComplaintType in the database
        List<CrbComplaintType> crbComplaintTypeList = crbComplaintTypeRepository.findAll();
        assertThat(crbComplaintTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbComplaintType in Elasticsearch
        verify(mockCrbComplaintTypeSearchRepository, times(0)).save(crbComplaintType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbComplaintType() throws Exception {
        int databaseSizeBeforeUpdate = crbComplaintTypeRepository.findAll().size();
        crbComplaintType.setId(count.incrementAndGet());

        // Create the CrbComplaintType
        CrbComplaintTypeDTO crbComplaintTypeDTO = crbComplaintTypeMapper.toDto(crbComplaintType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbComplaintTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbComplaintTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbComplaintType in the database
        List<CrbComplaintType> crbComplaintTypeList = crbComplaintTypeRepository.findAll();
        assertThat(crbComplaintTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbComplaintType in Elasticsearch
        verify(mockCrbComplaintTypeSearchRepository, times(0)).save(crbComplaintType);
    }

    @Test
    @Transactional
    void deleteCrbComplaintType() throws Exception {
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);

        int databaseSizeBeforeDelete = crbComplaintTypeRepository.findAll().size();

        // Delete the crbComplaintType
        restCrbComplaintTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbComplaintType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbComplaintType> crbComplaintTypeList = crbComplaintTypeRepository.findAll();
        assertThat(crbComplaintTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbComplaintType in Elasticsearch
        verify(mockCrbComplaintTypeSearchRepository, times(1)).deleteById(crbComplaintType.getId());
    }

    @Test
    @Transactional
    void searchCrbComplaintType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbComplaintTypeRepository.saveAndFlush(crbComplaintType);
        when(mockCrbComplaintTypeSearchRepository.search("id:" + crbComplaintType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbComplaintType), PageRequest.of(0, 1), 1));

        // Search the crbComplaintType
        restCrbComplaintTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbComplaintType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbComplaintType.getId().intValue())))
            .andExpect(jsonPath("$.[*].complaintTypeCode").value(hasItem(DEFAULT_COMPLAINT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].complaintType").value(hasItem(DEFAULT_COMPLAINT_TYPE)))
            .andExpect(jsonPath("$.[*].complaintTypeDetails").value(hasItem(DEFAULT_COMPLAINT_TYPE_DETAILS.toString())));
    }
}
