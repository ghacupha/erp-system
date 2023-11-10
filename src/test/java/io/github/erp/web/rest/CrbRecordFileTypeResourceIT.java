package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 2 (Gideon Series) Server ver 1.5.6
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
import io.github.erp.domain.CrbRecordFileType;
import io.github.erp.repository.CrbRecordFileTypeRepository;
import io.github.erp.repository.search.CrbRecordFileTypeSearchRepository;
import io.github.erp.service.criteria.CrbRecordFileTypeCriteria;
import io.github.erp.service.dto.CrbRecordFileTypeDTO;
import io.github.erp.service.mapper.CrbRecordFileTypeMapper;
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
 * Integration tests for the {@link CrbRecordFileTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CrbRecordFileTypeResourceIT {

    private static final String DEFAULT_RECORD_FILE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_RECORD_FILE_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_RECORD_FILE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_RECORD_FILE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_RECORD_FILE_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_RECORD_FILE_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crb-record-file-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/crb-record-file-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbRecordFileTypeRepository crbRecordFileTypeRepository;

    @Autowired
    private CrbRecordFileTypeMapper crbRecordFileTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbRecordFileTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbRecordFileTypeSearchRepository mockCrbRecordFileTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbRecordFileTypeMockMvc;

    private CrbRecordFileType crbRecordFileType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbRecordFileType createEntity(EntityManager em) {
        CrbRecordFileType crbRecordFileType = new CrbRecordFileType()
            .recordFileTypeCode(DEFAULT_RECORD_FILE_TYPE_CODE)
            .recordFileType(DEFAULT_RECORD_FILE_TYPE)
            .recordFileTypeDetails(DEFAULT_RECORD_FILE_TYPE_DETAILS);
        return crbRecordFileType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbRecordFileType createUpdatedEntity(EntityManager em) {
        CrbRecordFileType crbRecordFileType = new CrbRecordFileType()
            .recordFileTypeCode(UPDATED_RECORD_FILE_TYPE_CODE)
            .recordFileType(UPDATED_RECORD_FILE_TYPE)
            .recordFileTypeDetails(UPDATED_RECORD_FILE_TYPE_DETAILS);
        return crbRecordFileType;
    }

    @BeforeEach
    public void initTest() {
        crbRecordFileType = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbRecordFileType() throws Exception {
        int databaseSizeBeforeCreate = crbRecordFileTypeRepository.findAll().size();
        // Create the CrbRecordFileType
        CrbRecordFileTypeDTO crbRecordFileTypeDTO = crbRecordFileTypeMapper.toDto(crbRecordFileType);
        restCrbRecordFileTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbRecordFileTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbRecordFileType in the database
        List<CrbRecordFileType> crbRecordFileTypeList = crbRecordFileTypeRepository.findAll();
        assertThat(crbRecordFileTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CrbRecordFileType testCrbRecordFileType = crbRecordFileTypeList.get(crbRecordFileTypeList.size() - 1);
        assertThat(testCrbRecordFileType.getRecordFileTypeCode()).isEqualTo(DEFAULT_RECORD_FILE_TYPE_CODE);
        assertThat(testCrbRecordFileType.getRecordFileType()).isEqualTo(DEFAULT_RECORD_FILE_TYPE);
        assertThat(testCrbRecordFileType.getRecordFileTypeDetails()).isEqualTo(DEFAULT_RECORD_FILE_TYPE_DETAILS);

        // Validate the CrbRecordFileType in Elasticsearch
        verify(mockCrbRecordFileTypeSearchRepository, times(1)).save(testCrbRecordFileType);
    }

    @Test
    @Transactional
    void createCrbRecordFileTypeWithExistingId() throws Exception {
        // Create the CrbRecordFileType with an existing ID
        crbRecordFileType.setId(1L);
        CrbRecordFileTypeDTO crbRecordFileTypeDTO = crbRecordFileTypeMapper.toDto(crbRecordFileType);

        int databaseSizeBeforeCreate = crbRecordFileTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbRecordFileTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbRecordFileTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbRecordFileType in the database
        List<CrbRecordFileType> crbRecordFileTypeList = crbRecordFileTypeRepository.findAll();
        assertThat(crbRecordFileTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbRecordFileType in Elasticsearch
        verify(mockCrbRecordFileTypeSearchRepository, times(0)).save(crbRecordFileType);
    }

    @Test
    @Transactional
    void checkRecordFileTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbRecordFileTypeRepository.findAll().size();
        // set the field null
        crbRecordFileType.setRecordFileTypeCode(null);

        // Create the CrbRecordFileType, which fails.
        CrbRecordFileTypeDTO crbRecordFileTypeDTO = crbRecordFileTypeMapper.toDto(crbRecordFileType);

        restCrbRecordFileTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbRecordFileTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbRecordFileType> crbRecordFileTypeList = crbRecordFileTypeRepository.findAll();
        assertThat(crbRecordFileTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecordFileTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbRecordFileTypeRepository.findAll().size();
        // set the field null
        crbRecordFileType.setRecordFileType(null);

        // Create the CrbRecordFileType, which fails.
        CrbRecordFileTypeDTO crbRecordFileTypeDTO = crbRecordFileTypeMapper.toDto(crbRecordFileType);

        restCrbRecordFileTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbRecordFileTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbRecordFileType> crbRecordFileTypeList = crbRecordFileTypeRepository.findAll();
        assertThat(crbRecordFileTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbRecordFileTypes() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        // Get all the crbRecordFileTypeList
        restCrbRecordFileTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbRecordFileType.getId().intValue())))
            .andExpect(jsonPath("$.[*].recordFileTypeCode").value(hasItem(DEFAULT_RECORD_FILE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].recordFileType").value(hasItem(DEFAULT_RECORD_FILE_TYPE)))
            .andExpect(jsonPath("$.[*].recordFileTypeDetails").value(hasItem(DEFAULT_RECORD_FILE_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getCrbRecordFileType() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        // Get the crbRecordFileType
        restCrbRecordFileTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, crbRecordFileType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbRecordFileType.getId().intValue()))
            .andExpect(jsonPath("$.recordFileTypeCode").value(DEFAULT_RECORD_FILE_TYPE_CODE))
            .andExpect(jsonPath("$.recordFileType").value(DEFAULT_RECORD_FILE_TYPE))
            .andExpect(jsonPath("$.recordFileTypeDetails").value(DEFAULT_RECORD_FILE_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getCrbRecordFileTypesByIdFiltering() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        Long id = crbRecordFileType.getId();

        defaultCrbRecordFileTypeShouldBeFound("id.equals=" + id);
        defaultCrbRecordFileTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCrbRecordFileTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbRecordFileTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbRecordFileTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbRecordFileTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbRecordFileTypesByRecordFileTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        // Get all the crbRecordFileTypeList where recordFileTypeCode equals to DEFAULT_RECORD_FILE_TYPE_CODE
        defaultCrbRecordFileTypeShouldBeFound("recordFileTypeCode.equals=" + DEFAULT_RECORD_FILE_TYPE_CODE);

        // Get all the crbRecordFileTypeList where recordFileTypeCode equals to UPDATED_RECORD_FILE_TYPE_CODE
        defaultCrbRecordFileTypeShouldNotBeFound("recordFileTypeCode.equals=" + UPDATED_RECORD_FILE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbRecordFileTypesByRecordFileTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        // Get all the crbRecordFileTypeList where recordFileTypeCode not equals to DEFAULT_RECORD_FILE_TYPE_CODE
        defaultCrbRecordFileTypeShouldNotBeFound("recordFileTypeCode.notEquals=" + DEFAULT_RECORD_FILE_TYPE_CODE);

        // Get all the crbRecordFileTypeList where recordFileTypeCode not equals to UPDATED_RECORD_FILE_TYPE_CODE
        defaultCrbRecordFileTypeShouldBeFound("recordFileTypeCode.notEquals=" + UPDATED_RECORD_FILE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbRecordFileTypesByRecordFileTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        // Get all the crbRecordFileTypeList where recordFileTypeCode in DEFAULT_RECORD_FILE_TYPE_CODE or UPDATED_RECORD_FILE_TYPE_CODE
        defaultCrbRecordFileTypeShouldBeFound(
            "recordFileTypeCode.in=" + DEFAULT_RECORD_FILE_TYPE_CODE + "," + UPDATED_RECORD_FILE_TYPE_CODE
        );

        // Get all the crbRecordFileTypeList where recordFileTypeCode equals to UPDATED_RECORD_FILE_TYPE_CODE
        defaultCrbRecordFileTypeShouldNotBeFound("recordFileTypeCode.in=" + UPDATED_RECORD_FILE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbRecordFileTypesByRecordFileTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        // Get all the crbRecordFileTypeList where recordFileTypeCode is not null
        defaultCrbRecordFileTypeShouldBeFound("recordFileTypeCode.specified=true");

        // Get all the crbRecordFileTypeList where recordFileTypeCode is null
        defaultCrbRecordFileTypeShouldNotBeFound("recordFileTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbRecordFileTypesByRecordFileTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        // Get all the crbRecordFileTypeList where recordFileTypeCode contains DEFAULT_RECORD_FILE_TYPE_CODE
        defaultCrbRecordFileTypeShouldBeFound("recordFileTypeCode.contains=" + DEFAULT_RECORD_FILE_TYPE_CODE);

        // Get all the crbRecordFileTypeList where recordFileTypeCode contains UPDATED_RECORD_FILE_TYPE_CODE
        defaultCrbRecordFileTypeShouldNotBeFound("recordFileTypeCode.contains=" + UPDATED_RECORD_FILE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbRecordFileTypesByRecordFileTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        // Get all the crbRecordFileTypeList where recordFileTypeCode does not contain DEFAULT_RECORD_FILE_TYPE_CODE
        defaultCrbRecordFileTypeShouldNotBeFound("recordFileTypeCode.doesNotContain=" + DEFAULT_RECORD_FILE_TYPE_CODE);

        // Get all the crbRecordFileTypeList where recordFileTypeCode does not contain UPDATED_RECORD_FILE_TYPE_CODE
        defaultCrbRecordFileTypeShouldBeFound("recordFileTypeCode.doesNotContain=" + UPDATED_RECORD_FILE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbRecordFileTypesByRecordFileTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        // Get all the crbRecordFileTypeList where recordFileType equals to DEFAULT_RECORD_FILE_TYPE
        defaultCrbRecordFileTypeShouldBeFound("recordFileType.equals=" + DEFAULT_RECORD_FILE_TYPE);

        // Get all the crbRecordFileTypeList where recordFileType equals to UPDATED_RECORD_FILE_TYPE
        defaultCrbRecordFileTypeShouldNotBeFound("recordFileType.equals=" + UPDATED_RECORD_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbRecordFileTypesByRecordFileTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        // Get all the crbRecordFileTypeList where recordFileType not equals to DEFAULT_RECORD_FILE_TYPE
        defaultCrbRecordFileTypeShouldNotBeFound("recordFileType.notEquals=" + DEFAULT_RECORD_FILE_TYPE);

        // Get all the crbRecordFileTypeList where recordFileType not equals to UPDATED_RECORD_FILE_TYPE
        defaultCrbRecordFileTypeShouldBeFound("recordFileType.notEquals=" + UPDATED_RECORD_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbRecordFileTypesByRecordFileTypeIsInShouldWork() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        // Get all the crbRecordFileTypeList where recordFileType in DEFAULT_RECORD_FILE_TYPE or UPDATED_RECORD_FILE_TYPE
        defaultCrbRecordFileTypeShouldBeFound("recordFileType.in=" + DEFAULT_RECORD_FILE_TYPE + "," + UPDATED_RECORD_FILE_TYPE);

        // Get all the crbRecordFileTypeList where recordFileType equals to UPDATED_RECORD_FILE_TYPE
        defaultCrbRecordFileTypeShouldNotBeFound("recordFileType.in=" + UPDATED_RECORD_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbRecordFileTypesByRecordFileTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        // Get all the crbRecordFileTypeList where recordFileType is not null
        defaultCrbRecordFileTypeShouldBeFound("recordFileType.specified=true");

        // Get all the crbRecordFileTypeList where recordFileType is null
        defaultCrbRecordFileTypeShouldNotBeFound("recordFileType.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbRecordFileTypesByRecordFileTypeContainsSomething() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        // Get all the crbRecordFileTypeList where recordFileType contains DEFAULT_RECORD_FILE_TYPE
        defaultCrbRecordFileTypeShouldBeFound("recordFileType.contains=" + DEFAULT_RECORD_FILE_TYPE);

        // Get all the crbRecordFileTypeList where recordFileType contains UPDATED_RECORD_FILE_TYPE
        defaultCrbRecordFileTypeShouldNotBeFound("recordFileType.contains=" + UPDATED_RECORD_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbRecordFileTypesByRecordFileTypeNotContainsSomething() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        // Get all the crbRecordFileTypeList where recordFileType does not contain DEFAULT_RECORD_FILE_TYPE
        defaultCrbRecordFileTypeShouldNotBeFound("recordFileType.doesNotContain=" + DEFAULT_RECORD_FILE_TYPE);

        // Get all the crbRecordFileTypeList where recordFileType does not contain UPDATED_RECORD_FILE_TYPE
        defaultCrbRecordFileTypeShouldBeFound("recordFileType.doesNotContain=" + UPDATED_RECORD_FILE_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbRecordFileTypeShouldBeFound(String filter) throws Exception {
        restCrbRecordFileTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbRecordFileType.getId().intValue())))
            .andExpect(jsonPath("$.[*].recordFileTypeCode").value(hasItem(DEFAULT_RECORD_FILE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].recordFileType").value(hasItem(DEFAULT_RECORD_FILE_TYPE)))
            .andExpect(jsonPath("$.[*].recordFileTypeDetails").value(hasItem(DEFAULT_RECORD_FILE_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restCrbRecordFileTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbRecordFileTypeShouldNotBeFound(String filter) throws Exception {
        restCrbRecordFileTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbRecordFileTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbRecordFileType() throws Exception {
        // Get the crbRecordFileType
        restCrbRecordFileTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbRecordFileType() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        int databaseSizeBeforeUpdate = crbRecordFileTypeRepository.findAll().size();

        // Update the crbRecordFileType
        CrbRecordFileType updatedCrbRecordFileType = crbRecordFileTypeRepository.findById(crbRecordFileType.getId()).get();
        // Disconnect from session so that the updates on updatedCrbRecordFileType are not directly saved in db
        em.detach(updatedCrbRecordFileType);
        updatedCrbRecordFileType
            .recordFileTypeCode(UPDATED_RECORD_FILE_TYPE_CODE)
            .recordFileType(UPDATED_RECORD_FILE_TYPE)
            .recordFileTypeDetails(UPDATED_RECORD_FILE_TYPE_DETAILS);
        CrbRecordFileTypeDTO crbRecordFileTypeDTO = crbRecordFileTypeMapper.toDto(updatedCrbRecordFileType);

        restCrbRecordFileTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbRecordFileTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbRecordFileTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbRecordFileType in the database
        List<CrbRecordFileType> crbRecordFileTypeList = crbRecordFileTypeRepository.findAll();
        assertThat(crbRecordFileTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbRecordFileType testCrbRecordFileType = crbRecordFileTypeList.get(crbRecordFileTypeList.size() - 1);
        assertThat(testCrbRecordFileType.getRecordFileTypeCode()).isEqualTo(UPDATED_RECORD_FILE_TYPE_CODE);
        assertThat(testCrbRecordFileType.getRecordFileType()).isEqualTo(UPDATED_RECORD_FILE_TYPE);
        assertThat(testCrbRecordFileType.getRecordFileTypeDetails()).isEqualTo(UPDATED_RECORD_FILE_TYPE_DETAILS);

        // Validate the CrbRecordFileType in Elasticsearch
        verify(mockCrbRecordFileTypeSearchRepository).save(testCrbRecordFileType);
    }

    @Test
    @Transactional
    void putNonExistingCrbRecordFileType() throws Exception {
        int databaseSizeBeforeUpdate = crbRecordFileTypeRepository.findAll().size();
        crbRecordFileType.setId(count.incrementAndGet());

        // Create the CrbRecordFileType
        CrbRecordFileTypeDTO crbRecordFileTypeDTO = crbRecordFileTypeMapper.toDto(crbRecordFileType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbRecordFileTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbRecordFileTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbRecordFileTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbRecordFileType in the database
        List<CrbRecordFileType> crbRecordFileTypeList = crbRecordFileTypeRepository.findAll();
        assertThat(crbRecordFileTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbRecordFileType in Elasticsearch
        verify(mockCrbRecordFileTypeSearchRepository, times(0)).save(crbRecordFileType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbRecordFileType() throws Exception {
        int databaseSizeBeforeUpdate = crbRecordFileTypeRepository.findAll().size();
        crbRecordFileType.setId(count.incrementAndGet());

        // Create the CrbRecordFileType
        CrbRecordFileTypeDTO crbRecordFileTypeDTO = crbRecordFileTypeMapper.toDto(crbRecordFileType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbRecordFileTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbRecordFileTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbRecordFileType in the database
        List<CrbRecordFileType> crbRecordFileTypeList = crbRecordFileTypeRepository.findAll();
        assertThat(crbRecordFileTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbRecordFileType in Elasticsearch
        verify(mockCrbRecordFileTypeSearchRepository, times(0)).save(crbRecordFileType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbRecordFileType() throws Exception {
        int databaseSizeBeforeUpdate = crbRecordFileTypeRepository.findAll().size();
        crbRecordFileType.setId(count.incrementAndGet());

        // Create the CrbRecordFileType
        CrbRecordFileTypeDTO crbRecordFileTypeDTO = crbRecordFileTypeMapper.toDto(crbRecordFileType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbRecordFileTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbRecordFileTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbRecordFileType in the database
        List<CrbRecordFileType> crbRecordFileTypeList = crbRecordFileTypeRepository.findAll();
        assertThat(crbRecordFileTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbRecordFileType in Elasticsearch
        verify(mockCrbRecordFileTypeSearchRepository, times(0)).save(crbRecordFileType);
    }

    @Test
    @Transactional
    void partialUpdateCrbRecordFileTypeWithPatch() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        int databaseSizeBeforeUpdate = crbRecordFileTypeRepository.findAll().size();

        // Update the crbRecordFileType using partial update
        CrbRecordFileType partialUpdatedCrbRecordFileType = new CrbRecordFileType();
        partialUpdatedCrbRecordFileType.setId(crbRecordFileType.getId());

        partialUpdatedCrbRecordFileType.recordFileType(UPDATED_RECORD_FILE_TYPE);

        restCrbRecordFileTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbRecordFileType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbRecordFileType))
            )
            .andExpect(status().isOk());

        // Validate the CrbRecordFileType in the database
        List<CrbRecordFileType> crbRecordFileTypeList = crbRecordFileTypeRepository.findAll();
        assertThat(crbRecordFileTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbRecordFileType testCrbRecordFileType = crbRecordFileTypeList.get(crbRecordFileTypeList.size() - 1);
        assertThat(testCrbRecordFileType.getRecordFileTypeCode()).isEqualTo(DEFAULT_RECORD_FILE_TYPE_CODE);
        assertThat(testCrbRecordFileType.getRecordFileType()).isEqualTo(UPDATED_RECORD_FILE_TYPE);
        assertThat(testCrbRecordFileType.getRecordFileTypeDetails()).isEqualTo(DEFAULT_RECORD_FILE_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCrbRecordFileTypeWithPatch() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        int databaseSizeBeforeUpdate = crbRecordFileTypeRepository.findAll().size();

        // Update the crbRecordFileType using partial update
        CrbRecordFileType partialUpdatedCrbRecordFileType = new CrbRecordFileType();
        partialUpdatedCrbRecordFileType.setId(crbRecordFileType.getId());

        partialUpdatedCrbRecordFileType
            .recordFileTypeCode(UPDATED_RECORD_FILE_TYPE_CODE)
            .recordFileType(UPDATED_RECORD_FILE_TYPE)
            .recordFileTypeDetails(UPDATED_RECORD_FILE_TYPE_DETAILS);

        restCrbRecordFileTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbRecordFileType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbRecordFileType))
            )
            .andExpect(status().isOk());

        // Validate the CrbRecordFileType in the database
        List<CrbRecordFileType> crbRecordFileTypeList = crbRecordFileTypeRepository.findAll();
        assertThat(crbRecordFileTypeList).hasSize(databaseSizeBeforeUpdate);
        CrbRecordFileType testCrbRecordFileType = crbRecordFileTypeList.get(crbRecordFileTypeList.size() - 1);
        assertThat(testCrbRecordFileType.getRecordFileTypeCode()).isEqualTo(UPDATED_RECORD_FILE_TYPE_CODE);
        assertThat(testCrbRecordFileType.getRecordFileType()).isEqualTo(UPDATED_RECORD_FILE_TYPE);
        assertThat(testCrbRecordFileType.getRecordFileTypeDetails()).isEqualTo(UPDATED_RECORD_FILE_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCrbRecordFileType() throws Exception {
        int databaseSizeBeforeUpdate = crbRecordFileTypeRepository.findAll().size();
        crbRecordFileType.setId(count.incrementAndGet());

        // Create the CrbRecordFileType
        CrbRecordFileTypeDTO crbRecordFileTypeDTO = crbRecordFileTypeMapper.toDto(crbRecordFileType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbRecordFileTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbRecordFileTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbRecordFileTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbRecordFileType in the database
        List<CrbRecordFileType> crbRecordFileTypeList = crbRecordFileTypeRepository.findAll();
        assertThat(crbRecordFileTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbRecordFileType in Elasticsearch
        verify(mockCrbRecordFileTypeSearchRepository, times(0)).save(crbRecordFileType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbRecordFileType() throws Exception {
        int databaseSizeBeforeUpdate = crbRecordFileTypeRepository.findAll().size();
        crbRecordFileType.setId(count.incrementAndGet());

        // Create the CrbRecordFileType
        CrbRecordFileTypeDTO crbRecordFileTypeDTO = crbRecordFileTypeMapper.toDto(crbRecordFileType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbRecordFileTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbRecordFileTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbRecordFileType in the database
        List<CrbRecordFileType> crbRecordFileTypeList = crbRecordFileTypeRepository.findAll();
        assertThat(crbRecordFileTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbRecordFileType in Elasticsearch
        verify(mockCrbRecordFileTypeSearchRepository, times(0)).save(crbRecordFileType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbRecordFileType() throws Exception {
        int databaseSizeBeforeUpdate = crbRecordFileTypeRepository.findAll().size();
        crbRecordFileType.setId(count.incrementAndGet());

        // Create the CrbRecordFileType
        CrbRecordFileTypeDTO crbRecordFileTypeDTO = crbRecordFileTypeMapper.toDto(crbRecordFileType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbRecordFileTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbRecordFileTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbRecordFileType in the database
        List<CrbRecordFileType> crbRecordFileTypeList = crbRecordFileTypeRepository.findAll();
        assertThat(crbRecordFileTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbRecordFileType in Elasticsearch
        verify(mockCrbRecordFileTypeSearchRepository, times(0)).save(crbRecordFileType);
    }

    @Test
    @Transactional
    void deleteCrbRecordFileType() throws Exception {
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);

        int databaseSizeBeforeDelete = crbRecordFileTypeRepository.findAll().size();

        // Delete the crbRecordFileType
        restCrbRecordFileTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbRecordFileType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbRecordFileType> crbRecordFileTypeList = crbRecordFileTypeRepository.findAll();
        assertThat(crbRecordFileTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbRecordFileType in Elasticsearch
        verify(mockCrbRecordFileTypeSearchRepository, times(1)).deleteById(crbRecordFileType.getId());
    }

    @Test
    @Transactional
    void searchCrbRecordFileType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbRecordFileTypeRepository.saveAndFlush(crbRecordFileType);
        when(mockCrbRecordFileTypeSearchRepository.search("id:" + crbRecordFileType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbRecordFileType), PageRequest.of(0, 1), 1));

        // Search the crbRecordFileType
        restCrbRecordFileTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbRecordFileType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbRecordFileType.getId().intValue())))
            .andExpect(jsonPath("$.[*].recordFileTypeCode").value(hasItem(DEFAULT_RECORD_FILE_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].recordFileType").value(hasItem(DEFAULT_RECORD_FILE_TYPE)))
            .andExpect(jsonPath("$.[*].recordFileTypeDetails").value(hasItem(DEFAULT_RECORD_FILE_TYPE_DETAILS.toString())));
    }
}
