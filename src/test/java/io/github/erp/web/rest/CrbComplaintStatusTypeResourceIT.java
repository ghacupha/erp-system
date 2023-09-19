package io.github.erp.web.rest;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
import io.github.erp.domain.CrbComplaintStatusType;
import io.github.erp.repository.CrbComplaintStatusTypeRepository;
import io.github.erp.repository.search.CrbComplaintStatusTypeSearchRepository;
import io.github.erp.service.criteria.CrbComplaintStatusTypeCriteria;
import io.github.erp.service.dto.CrbComplaintStatusTypeDTO;
import io.github.erp.service.mapper.CrbComplaintStatusTypeMapper;
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
 * Integration tests for the {@link CrbComplaintStatusTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CrbComplaintStatusTypeResourceIT {

    private static final String DEFAULT_COMPLAINT_STATUS_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COMPLAINT_STATUS_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPLAINT_STATUS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_COMPLAINT_STATUS_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPLAINT_STATUS_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_COMPLAINT_STATUS_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crb-complaint-status-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/crb-complaint-status-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbComplaintStatusTypeRepository crbComplaintStatusTypeRepository;

    @Autowired
    private CrbComplaintStatusTypeMapper crbComplaintStatusTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbComplaintStatusTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbComplaintStatusTypeSearchRepository mockCrbComplaintStatusTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbComplaintStatusTypeMockMvc;

    private CrbComplaintStatusType crbComplaintStatusType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbComplaintStatusType createEntity(EntityManager em) {
        CrbComplaintStatusType crbComplaintStatusType = new CrbComplaintStatusType()
            .complaintStatusTypeCode(DEFAULT_COMPLAINT_STATUS_TYPE_CODE)
            .complaintStatusType(DEFAULT_COMPLAINT_STATUS_TYPE)
            .complaintStatusDetails(DEFAULT_COMPLAINT_STATUS_DETAILS);
        return crbComplaintStatusType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbComplaintStatusType createUpdatedEntity(EntityManager em) {
        CrbComplaintStatusType crbComplaintStatusType = new CrbComplaintStatusType()
            .complaintStatusTypeCode(UPDATED_COMPLAINT_STATUS_TYPE_CODE)
            .complaintStatusType(UPDATED_COMPLAINT_STATUS_TYPE)
            .complaintStatusDetails(UPDATED_COMPLAINT_STATUS_DETAILS);
        return crbComplaintStatusType;
    }

    @BeforeEach
    public void initTest() {
        crbComplaintStatusType = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbComplaintStatusType() throws Exception {
        int databaseSizeBeforeCreate = crbComplaintStatusTypeRepository.findAll().size();
        // Create the CrbComplaintStatusType
        CrbComplaintStatusTypeDTO crbComplaintStatusTypeDTO = crbComplaintStatusTypeMapper.toDto(crbComplaintStatusType);
        restCrbComplaintStatusTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbComplaintStatusTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbComplaintStatusType in the database
        List<CrbComplaintStatusType> crbComplaintStatusTypeList = crbComplaintStatusTypeRepository.findAll();
        assertThat(crbComplaintStatusTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CrbComplaintStatusType testCrbComplaintStatusType = crbComplaintStatusTypeList.get(crbComplaintStatusTypeList.size() - 1);
        assertThat(testCrbComplaintStatusType.getComplaintStatusTypeCode()).isEqualTo(DEFAULT_COMPLAINT_STATUS_TYPE_CODE);
        assertThat(testCrbComplaintStatusType.getComplaintStatusType()).isEqualTo(DEFAULT_COMPLAINT_STATUS_TYPE);
        assertThat(testCrbComplaintStatusType.getComplaintStatusDetails()).isEqualTo(DEFAULT_COMPLAINT_STATUS_DETAILS);

        // Validate the CrbComplaintStatusType in Elasticsearch
        verify(mockCrbComplaintStatusTypeSearchRepository, times(1)).save(testCrbComplaintStatusType);
    }

    @Test
    @Transactional
    void createCrbComplaintStatusTypeWithExistingId() throws Exception {
        // Create the CrbComplaintStatusType with an existing ID
        crbComplaintStatusType.setId(1L);
        CrbComplaintStatusTypeDTO crbComplaintStatusTypeDTO = crbComplaintStatusTypeMapper.toDto(crbComplaintStatusType);

        int databaseSizeBeforeCreate = crbComplaintStatusTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbComplaintStatusTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbComplaintStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbComplaintStatusType in the database
        List<CrbComplaintStatusType> crbComplaintStatusTypeList = crbComplaintStatusTypeRepository.findAll();
        assertThat(crbComplaintStatusTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbComplaintStatusType in Elasticsearch
        verify(mockCrbComplaintStatusTypeSearchRepository, times(0)).save(crbComplaintStatusType);
    }

    @Test
    @Transactional
    void checkComplaintStatusTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbComplaintStatusTypeRepository.findAll().size();
        // set the field null
        crbComplaintStatusType.setComplaintStatusTypeCode(null);

        // Create the CrbComplaintStatusType, which fails.
        CrbComplaintStatusTypeDTO crbComplaintStatusTypeDTO = crbComplaintStatusTypeMapper.toDto(crbComplaintStatusType);

        restCrbComplaintStatusTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbComplaintStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbComplaintStatusType> crbComplaintStatusTypeList = crbComplaintStatusTypeRepository.findAll();
        assertThat(crbComplaintStatusTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkComplaintStatusTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbComplaintStatusTypeRepository.findAll().size();
        // set the field null
        crbComplaintStatusType.setComplaintStatusType(null);

        // Create the CrbComplaintStatusType, which fails.
        CrbComplaintStatusTypeDTO crbComplaintStatusTypeDTO = crbComplaintStatusTypeMapper.toDto(crbComplaintStatusType);

        restCrbComplaintStatusTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbComplaintStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbComplaintStatusType> crbComplaintStatusTypeList = crbComplaintStatusTypeRepository.findAll();
        assertThat(crbComplaintStatusTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbComplaintStatusTypes() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        // Get all the crbComplaintStatusTypeList
        restCrbComplaintStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbComplaintStatusType.getId().intValue())))
            .andExpect(jsonPath("$.[*].complaintStatusTypeCode").value(hasItem(DEFAULT_COMPLAINT_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].complaintStatusType").value(hasItem(DEFAULT_COMPLAINT_STATUS_TYPE)))
            .andExpect(jsonPath("$.[*].complaintStatusDetails").value(hasItem(DEFAULT_COMPLAINT_STATUS_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getCrbComplaintStatusType() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        // Get the crbComplaintStatusType
        restCrbComplaintStatusTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, crbComplaintStatusType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbComplaintStatusType.getId().intValue()))
            .andExpect(jsonPath("$.complaintStatusTypeCode").value(DEFAULT_COMPLAINT_STATUS_TYPE_CODE))
            .andExpect(jsonPath("$.complaintStatusType").value(DEFAULT_COMPLAINT_STATUS_TYPE))
            .andExpect(jsonPath("$.complaintStatusDetails").value(DEFAULT_COMPLAINT_STATUS_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getCrbComplaintStatusTypesByIdFiltering() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        Long id = crbComplaintStatusType.getId();

        defaultCrbComplaintStatusTypeShouldBeFound("id.equals=" + id);
        defaultCrbComplaintStatusTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCrbComplaintStatusTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbComplaintStatusTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbComplaintStatusTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbComplaintStatusTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbComplaintStatusTypesByComplaintStatusTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        // Get all the crbComplaintStatusTypeList where complaintStatusTypeCode equals to DEFAULT_COMPLAINT_STATUS_TYPE_CODE
        defaultCrbComplaintStatusTypeShouldBeFound("complaintStatusTypeCode.equals=" + DEFAULT_COMPLAINT_STATUS_TYPE_CODE);

        // Get all the crbComplaintStatusTypeList where complaintStatusTypeCode equals to UPDATED_COMPLAINT_STATUS_TYPE_CODE
        defaultCrbComplaintStatusTypeShouldNotBeFound("complaintStatusTypeCode.equals=" + UPDATED_COMPLAINT_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbComplaintStatusTypesByComplaintStatusTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        // Get all the crbComplaintStatusTypeList where complaintStatusTypeCode not equals to DEFAULT_COMPLAINT_STATUS_TYPE_CODE
        defaultCrbComplaintStatusTypeShouldNotBeFound("complaintStatusTypeCode.notEquals=" + DEFAULT_COMPLAINT_STATUS_TYPE_CODE);

        // Get all the crbComplaintStatusTypeList where complaintStatusTypeCode not equals to UPDATED_COMPLAINT_STATUS_TYPE_CODE
        defaultCrbComplaintStatusTypeShouldBeFound("complaintStatusTypeCode.notEquals=" + UPDATED_COMPLAINT_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbComplaintStatusTypesByComplaintStatusTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        // Get all the crbComplaintStatusTypeList where complaintStatusTypeCode in DEFAULT_COMPLAINT_STATUS_TYPE_CODE or UPDATED_COMPLAINT_STATUS_TYPE_CODE
        defaultCrbComplaintStatusTypeShouldBeFound(
            "complaintStatusTypeCode.in=" + DEFAULT_COMPLAINT_STATUS_TYPE_CODE + "," + UPDATED_COMPLAINT_STATUS_TYPE_CODE
        );

        // Get all the crbComplaintStatusTypeList where complaintStatusTypeCode equals to UPDATED_COMPLAINT_STATUS_TYPE_CODE
        defaultCrbComplaintStatusTypeShouldNotBeFound("complaintStatusTypeCode.in=" + UPDATED_COMPLAINT_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbComplaintStatusTypesByComplaintStatusTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        // Get all the crbComplaintStatusTypeList where complaintStatusTypeCode is not null
        defaultCrbComplaintStatusTypeShouldBeFound("complaintStatusTypeCode.specified=true");

        // Get all the crbComplaintStatusTypeList where complaintStatusTypeCode is null
        defaultCrbComplaintStatusTypeShouldNotBeFound("complaintStatusTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbComplaintStatusTypesByComplaintStatusTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        // Get all the crbComplaintStatusTypeList where complaintStatusTypeCode contains DEFAULT_COMPLAINT_STATUS_TYPE_CODE
        defaultCrbComplaintStatusTypeShouldBeFound("complaintStatusTypeCode.contains=" + DEFAULT_COMPLAINT_STATUS_TYPE_CODE);

        // Get all the crbComplaintStatusTypeList where complaintStatusTypeCode contains UPDATED_COMPLAINT_STATUS_TYPE_CODE
        defaultCrbComplaintStatusTypeShouldNotBeFound("complaintStatusTypeCode.contains=" + UPDATED_COMPLAINT_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbComplaintStatusTypesByComplaintStatusTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        // Get all the crbComplaintStatusTypeList where complaintStatusTypeCode does not contain DEFAULT_COMPLAINT_STATUS_TYPE_CODE
        defaultCrbComplaintStatusTypeShouldNotBeFound("complaintStatusTypeCode.doesNotContain=" + DEFAULT_COMPLAINT_STATUS_TYPE_CODE);

        // Get all the crbComplaintStatusTypeList where complaintStatusTypeCode does not contain UPDATED_COMPLAINT_STATUS_TYPE_CODE
        defaultCrbComplaintStatusTypeShouldBeFound("complaintStatusTypeCode.doesNotContain=" + UPDATED_COMPLAINT_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbComplaintStatusTypesByComplaintStatusTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        // Get all the crbComplaintStatusTypeList where complaintStatusType equals to DEFAULT_COMPLAINT_STATUS_TYPE
        defaultCrbComplaintStatusTypeShouldBeFound("complaintStatusType.equals=" + DEFAULT_COMPLAINT_STATUS_TYPE);

        // Get all the crbComplaintStatusTypeList where complaintStatusType equals to UPDATED_COMPLAINT_STATUS_TYPE
        defaultCrbComplaintStatusTypeShouldNotBeFound("complaintStatusType.equals=" + UPDATED_COMPLAINT_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbComplaintStatusTypesByComplaintStatusTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        // Get all the crbComplaintStatusTypeList where complaintStatusType not equals to DEFAULT_COMPLAINT_STATUS_TYPE
        defaultCrbComplaintStatusTypeShouldNotBeFound("complaintStatusType.notEquals=" + DEFAULT_COMPLAINT_STATUS_TYPE);

        // Get all the crbComplaintStatusTypeList where complaintStatusType not equals to UPDATED_COMPLAINT_STATUS_TYPE
        defaultCrbComplaintStatusTypeShouldBeFound("complaintStatusType.notEquals=" + UPDATED_COMPLAINT_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbComplaintStatusTypesByComplaintStatusTypeIsInShouldWork() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        // Get all the crbComplaintStatusTypeList where complaintStatusType in DEFAULT_COMPLAINT_STATUS_TYPE or UPDATED_COMPLAINT_STATUS_TYPE
        defaultCrbComplaintStatusTypeShouldBeFound(
            "complaintStatusType.in=" + DEFAULT_COMPLAINT_STATUS_TYPE + "," + UPDATED_COMPLAINT_STATUS_TYPE
        );

        // Get all the crbComplaintStatusTypeList where complaintStatusType equals to UPDATED_COMPLAINT_STATUS_TYPE
        defaultCrbComplaintStatusTypeShouldNotBeFound("complaintStatusType.in=" + UPDATED_COMPLAINT_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbComplaintStatusTypesByComplaintStatusTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        // Get all the crbComplaintStatusTypeList where complaintStatusType is not null
        defaultCrbComplaintStatusTypeShouldBeFound("complaintStatusType.specified=true");

        // Get all the crbComplaintStatusTypeList where complaintStatusType is null
        defaultCrbComplaintStatusTypeShouldNotBeFound("complaintStatusType.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbComplaintStatusTypesByComplaintStatusTypeContainsSomething() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        // Get all the crbComplaintStatusTypeList where complaintStatusType contains DEFAULT_COMPLAINT_STATUS_TYPE
        defaultCrbComplaintStatusTypeShouldBeFound("complaintStatusType.contains=" + DEFAULT_COMPLAINT_STATUS_TYPE);

        // Get all the crbComplaintStatusTypeList where complaintStatusType contains UPDATED_COMPLAINT_STATUS_TYPE
        defaultCrbComplaintStatusTypeShouldNotBeFound("complaintStatusType.contains=" + UPDATED_COMPLAINT_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbComplaintStatusTypesByComplaintStatusTypeNotContainsSomething() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        // Get all the crbComplaintStatusTypeList where complaintStatusType does not contain DEFAULT_COMPLAINT_STATUS_TYPE
        defaultCrbComplaintStatusTypeShouldNotBeFound("complaintStatusType.doesNotContain=" + DEFAULT_COMPLAINT_STATUS_TYPE);

        // Get all the crbComplaintStatusTypeList where complaintStatusType does not contain UPDATED_COMPLAINT_STATUS_TYPE
        defaultCrbComplaintStatusTypeShouldBeFound("complaintStatusType.doesNotContain=" + UPDATED_COMPLAINT_STATUS_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbComplaintStatusTypeShouldBeFound(String filter) throws Exception {
        restCrbComplaintStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbComplaintStatusType.getId().intValue())))
            .andExpect(jsonPath("$.[*].complaintStatusTypeCode").value(hasItem(DEFAULT_COMPLAINT_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].complaintStatusType").value(hasItem(DEFAULT_COMPLAINT_STATUS_TYPE)))
            .andExpect(jsonPath("$.[*].complaintStatusDetails").value(hasItem(DEFAULT_COMPLAINT_STATUS_DETAILS.toString())));

        // Check, that the count call also returns 1
        restCrbComplaintStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbComplaintStatusTypeShouldNotBeFound(String filter) throws Exception {
        restCrbComplaintStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbComplaintStatusTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbComplaintStatusType() throws Exception {
        // Get the crbComplaintStatusType
        restCrbComplaintStatusTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbComplaintStatusType() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        int databaseSizeBeforeUpdate = crbComplaintStatusTypeRepository.findAll().size();

        // Update the crbComplaintStatusType
        CrbComplaintStatusType updatedCrbComplaintStatusType = crbComplaintStatusTypeRepository
            .findById(crbComplaintStatusType.getId())
            .get();
        // Disconnect from session so that the updates on updatedCrbComplaintStatusType are not directly saved in db
        em.detach(updatedCrbComplaintStatusType);
        updatedCrbComplaintStatusType
            .complaintStatusTypeCode(UPDATED_COMPLAINT_STATUS_TYPE_CODE)
            .complaintStatusType(UPDATED_COMPLAINT_STATUS_TYPE)
            .complaintStatusDetails(UPDATED_COMPLAINT_STATUS_DETAILS);
        CrbComplaintStatusTypeDTO crbComplaintStatusTypeDTO = crbComplaintStatusTypeMapper.toDto(updatedCrbComplaintStatusType);

        restCrbComplaintStatusTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbComplaintStatusTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbComplaintStatusTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbComplaintStatusType in the database
        List<CrbComplaintStatusType> crbComplaintStatusTypeList = crbComplaintStatusTypeRepository.findAll();
        assertThat(crbComplaintStatusTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbComplaintStatusType testCrbComplaintStatusType = crbComplaintStatusTypeList.get(crbComplaintStatusTypeList.size() - 1);
        assertThat(testCrbComplaintStatusType.getComplaintStatusTypeCode()).isEqualTo(UPDATED_COMPLAINT_STATUS_TYPE_CODE);
        assertThat(testCrbComplaintStatusType.getComplaintStatusType()).isEqualTo(UPDATED_COMPLAINT_STATUS_TYPE);
        assertThat(testCrbComplaintStatusType.getComplaintStatusDetails()).isEqualTo(UPDATED_COMPLAINT_STATUS_DETAILS);

        // Validate the CrbComplaintStatusType in Elasticsearch
        verify(mockCrbComplaintStatusTypeSearchRepository).save(testCrbComplaintStatusType);
    }

    @Test
    @Transactional
    void putNonExistingCrbComplaintStatusType() throws Exception {
        int databaseSizeBeforeUpdate = crbComplaintStatusTypeRepository.findAll().size();
        crbComplaintStatusType.setId(count.incrementAndGet());

        // Create the CrbComplaintStatusType
        CrbComplaintStatusTypeDTO crbComplaintStatusTypeDTO = crbComplaintStatusTypeMapper.toDto(crbComplaintStatusType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbComplaintStatusTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbComplaintStatusTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbComplaintStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbComplaintStatusType in the database
        List<CrbComplaintStatusType> crbComplaintStatusTypeList = crbComplaintStatusTypeRepository.findAll();
        assertThat(crbComplaintStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbComplaintStatusType in Elasticsearch
        verify(mockCrbComplaintStatusTypeSearchRepository, times(0)).save(crbComplaintStatusType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbComplaintStatusType() throws Exception {
        int databaseSizeBeforeUpdate = crbComplaintStatusTypeRepository.findAll().size();
        crbComplaintStatusType.setId(count.incrementAndGet());

        // Create the CrbComplaintStatusType
        CrbComplaintStatusTypeDTO crbComplaintStatusTypeDTO = crbComplaintStatusTypeMapper.toDto(crbComplaintStatusType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbComplaintStatusTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbComplaintStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbComplaintStatusType in the database
        List<CrbComplaintStatusType> crbComplaintStatusTypeList = crbComplaintStatusTypeRepository.findAll();
        assertThat(crbComplaintStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbComplaintStatusType in Elasticsearch
        verify(mockCrbComplaintStatusTypeSearchRepository, times(0)).save(crbComplaintStatusType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbComplaintStatusType() throws Exception {
        int databaseSizeBeforeUpdate = crbComplaintStatusTypeRepository.findAll().size();
        crbComplaintStatusType.setId(count.incrementAndGet());

        // Create the CrbComplaintStatusType
        CrbComplaintStatusTypeDTO crbComplaintStatusTypeDTO = crbComplaintStatusTypeMapper.toDto(crbComplaintStatusType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbComplaintStatusTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbComplaintStatusTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbComplaintStatusType in the database
        List<CrbComplaintStatusType> crbComplaintStatusTypeList = crbComplaintStatusTypeRepository.findAll();
        assertThat(crbComplaintStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbComplaintStatusType in Elasticsearch
        verify(mockCrbComplaintStatusTypeSearchRepository, times(0)).save(crbComplaintStatusType);
    }

    @Test
    @Transactional
    void partialUpdateCrbComplaintStatusTypeWithPatch() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        int databaseSizeBeforeUpdate = crbComplaintStatusTypeRepository.findAll().size();

        // Update the crbComplaintStatusType using partial update
        CrbComplaintStatusType partialUpdatedCrbComplaintStatusType = new CrbComplaintStatusType();
        partialUpdatedCrbComplaintStatusType.setId(crbComplaintStatusType.getId());

        restCrbComplaintStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbComplaintStatusType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbComplaintStatusType))
            )
            .andExpect(status().isOk());

        // Validate the CrbComplaintStatusType in the database
        List<CrbComplaintStatusType> crbComplaintStatusTypeList = crbComplaintStatusTypeRepository.findAll();
        assertThat(crbComplaintStatusTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbComplaintStatusType testCrbComplaintStatusType = crbComplaintStatusTypeList.get(crbComplaintStatusTypeList.size() - 1);
        assertThat(testCrbComplaintStatusType.getComplaintStatusTypeCode()).isEqualTo(DEFAULT_COMPLAINT_STATUS_TYPE_CODE);
        assertThat(testCrbComplaintStatusType.getComplaintStatusType()).isEqualTo(DEFAULT_COMPLAINT_STATUS_TYPE);
        assertThat(testCrbComplaintStatusType.getComplaintStatusDetails()).isEqualTo(DEFAULT_COMPLAINT_STATUS_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCrbComplaintStatusTypeWithPatch() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        int databaseSizeBeforeUpdate = crbComplaintStatusTypeRepository.findAll().size();

        // Update the crbComplaintStatusType using partial update
        CrbComplaintStatusType partialUpdatedCrbComplaintStatusType = new CrbComplaintStatusType();
        partialUpdatedCrbComplaintStatusType.setId(crbComplaintStatusType.getId());

        partialUpdatedCrbComplaintStatusType
            .complaintStatusTypeCode(UPDATED_COMPLAINT_STATUS_TYPE_CODE)
            .complaintStatusType(UPDATED_COMPLAINT_STATUS_TYPE)
            .complaintStatusDetails(UPDATED_COMPLAINT_STATUS_DETAILS);

        restCrbComplaintStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbComplaintStatusType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbComplaintStatusType))
            )
            .andExpect(status().isOk());

        // Validate the CrbComplaintStatusType in the database
        List<CrbComplaintStatusType> crbComplaintStatusTypeList = crbComplaintStatusTypeRepository.findAll();
        assertThat(crbComplaintStatusTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbComplaintStatusType testCrbComplaintStatusType = crbComplaintStatusTypeList.get(crbComplaintStatusTypeList.size() - 1);
        assertThat(testCrbComplaintStatusType.getComplaintStatusTypeCode()).isEqualTo(UPDATED_COMPLAINT_STATUS_TYPE_CODE);
        assertThat(testCrbComplaintStatusType.getComplaintStatusType()).isEqualTo(UPDATED_COMPLAINT_STATUS_TYPE);
        assertThat(testCrbComplaintStatusType.getComplaintStatusDetails()).isEqualTo(UPDATED_COMPLAINT_STATUS_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCrbComplaintStatusType() throws Exception {
        int databaseSizeBeforeUpdate = crbComplaintStatusTypeRepository.findAll().size();
        crbComplaintStatusType.setId(count.incrementAndGet());

        // Create the CrbComplaintStatusType
        CrbComplaintStatusTypeDTO crbComplaintStatusTypeDTO = crbComplaintStatusTypeMapper.toDto(crbComplaintStatusType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbComplaintStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbComplaintStatusTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbComplaintStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbComplaintStatusType in the database
        List<CrbComplaintStatusType> crbComplaintStatusTypeList = crbComplaintStatusTypeRepository.findAll();
        assertThat(crbComplaintStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbComplaintStatusType in Elasticsearch
        verify(mockCrbComplaintStatusTypeSearchRepository, times(0)).save(crbComplaintStatusType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbComplaintStatusType() throws Exception {
        int databaseSizeBeforeUpdate = crbComplaintStatusTypeRepository.findAll().size();
        crbComplaintStatusType.setId(count.incrementAndGet());

        // Create the CrbComplaintStatusType
        CrbComplaintStatusTypeDTO crbComplaintStatusTypeDTO = crbComplaintStatusTypeMapper.toDto(crbComplaintStatusType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbComplaintStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbComplaintStatusTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbComplaintStatusType in the database
        List<CrbComplaintStatusType> crbComplaintStatusTypeList = crbComplaintStatusTypeRepository.findAll();
        assertThat(crbComplaintStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbComplaintStatusType in Elasticsearch
        verify(mockCrbComplaintStatusTypeSearchRepository, times(0)).save(crbComplaintStatusType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbComplaintStatusType() throws Exception {
        int databaseSizeBeforeUpdate = crbComplaintStatusTypeRepository.findAll().size();
        crbComplaintStatusType.setId(count.incrementAndGet());

        // Create the CrbComplaintStatusType
        CrbComplaintStatusTypeDTO crbComplaintStatusTypeDTO = crbComplaintStatusTypeMapper.toDto(crbComplaintStatusType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbComplaintStatusTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbComplaintStatusTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbComplaintStatusType in the database
        List<CrbComplaintStatusType> crbComplaintStatusTypeList = crbComplaintStatusTypeRepository.findAll();
        assertThat(crbComplaintStatusTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbComplaintStatusType in Elasticsearch
        verify(mockCrbComplaintStatusTypeSearchRepository, times(0)).save(crbComplaintStatusType);
    }

    @Test
    @Transactional
    void deleteCrbComplaintStatusType() throws Exception {
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);

        int databaseSizeBeforeDelete = crbComplaintStatusTypeRepository.findAll().size();

        // Delete the crbComplaintStatusType
        restCrbComplaintStatusTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbComplaintStatusType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbComplaintStatusType> crbComplaintStatusTypeList = crbComplaintStatusTypeRepository.findAll();
        assertThat(crbComplaintStatusTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbComplaintStatusType in Elasticsearch
        verify(mockCrbComplaintStatusTypeSearchRepository, times(1)).deleteById(crbComplaintStatusType.getId());
    }

    @Test
    @Transactional
    void searchCrbComplaintStatusType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbComplaintStatusTypeRepository.saveAndFlush(crbComplaintStatusType);
        when(mockCrbComplaintStatusTypeSearchRepository.search("id:" + crbComplaintStatusType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbComplaintStatusType), PageRequest.of(0, 1), 1));

        // Search the crbComplaintStatusType
        restCrbComplaintStatusTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbComplaintStatusType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbComplaintStatusType.getId().intValue())))
            .andExpect(jsonPath("$.[*].complaintStatusTypeCode").value(hasItem(DEFAULT_COMPLAINT_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].complaintStatusType").value(hasItem(DEFAULT_COMPLAINT_STATUS_TYPE)))
            .andExpect(jsonPath("$.[*].complaintStatusDetails").value(hasItem(DEFAULT_COMPLAINT_STATUS_DETAILS.toString())));
    }
}
