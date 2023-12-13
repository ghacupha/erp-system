package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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
import io.github.erp.domain.SourcesOfFundsTypeCode;
import io.github.erp.repository.SourcesOfFundsTypeCodeRepository;
import io.github.erp.repository.search.SourcesOfFundsTypeCodeSearchRepository;
import io.github.erp.service.criteria.SourcesOfFundsTypeCodeCriteria;
import io.github.erp.service.dto.SourcesOfFundsTypeCodeDTO;
import io.github.erp.service.mapper.SourcesOfFundsTypeCodeMapper;
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
 * Integration tests for the {@link SourcesOfFundsTypeCodeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SourcesOfFundsTypeCodeResourceIT {

    private static final String DEFAULT_SOURCE_OF_FUNDS_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_OF_FUNDS_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_OF_FUNDS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_OF_FUNDS_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_OF_FUNDS_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_OF_FUNDS_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sources-of-funds-type-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/sources-of-funds-type-codes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SourcesOfFundsTypeCodeRepository sourcesOfFundsTypeCodeRepository;

    @Autowired
    private SourcesOfFundsTypeCodeMapper sourcesOfFundsTypeCodeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.SourcesOfFundsTypeCodeSearchRepositoryMockConfiguration
     */
    @Autowired
    private SourcesOfFundsTypeCodeSearchRepository mockSourcesOfFundsTypeCodeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSourcesOfFundsTypeCodeMockMvc;

    private SourcesOfFundsTypeCode sourcesOfFundsTypeCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourcesOfFundsTypeCode createEntity(EntityManager em) {
        SourcesOfFundsTypeCode sourcesOfFundsTypeCode = new SourcesOfFundsTypeCode()
            .sourceOfFundsTypeCode(DEFAULT_SOURCE_OF_FUNDS_TYPE_CODE)
            .sourceOfFundsType(DEFAULT_SOURCE_OF_FUNDS_TYPE)
            .sourceOfFundsTypeDetails(DEFAULT_SOURCE_OF_FUNDS_TYPE_DETAILS);
        return sourcesOfFundsTypeCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourcesOfFundsTypeCode createUpdatedEntity(EntityManager em) {
        SourcesOfFundsTypeCode sourcesOfFundsTypeCode = new SourcesOfFundsTypeCode()
            .sourceOfFundsTypeCode(UPDATED_SOURCE_OF_FUNDS_TYPE_CODE)
            .sourceOfFundsType(UPDATED_SOURCE_OF_FUNDS_TYPE)
            .sourceOfFundsTypeDetails(UPDATED_SOURCE_OF_FUNDS_TYPE_DETAILS);
        return sourcesOfFundsTypeCode;
    }

    @BeforeEach
    public void initTest() {
        sourcesOfFundsTypeCode = createEntity(em);
    }

    @Test
    @Transactional
    void createSourcesOfFundsTypeCode() throws Exception {
        int databaseSizeBeforeCreate = sourcesOfFundsTypeCodeRepository.findAll().size();
        // Create the SourcesOfFundsTypeCode
        SourcesOfFundsTypeCodeDTO sourcesOfFundsTypeCodeDTO = sourcesOfFundsTypeCodeMapper.toDto(sourcesOfFundsTypeCode);
        restSourcesOfFundsTypeCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourcesOfFundsTypeCodeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SourcesOfFundsTypeCode in the database
        List<SourcesOfFundsTypeCode> sourcesOfFundsTypeCodeList = sourcesOfFundsTypeCodeRepository.findAll();
        assertThat(sourcesOfFundsTypeCodeList).hasSize(databaseSizeBeforeCreate + 1);
        SourcesOfFundsTypeCode testSourcesOfFundsTypeCode = sourcesOfFundsTypeCodeList.get(sourcesOfFundsTypeCodeList.size() - 1);
        assertThat(testSourcesOfFundsTypeCode.getSourceOfFundsTypeCode()).isEqualTo(DEFAULT_SOURCE_OF_FUNDS_TYPE_CODE);
        assertThat(testSourcesOfFundsTypeCode.getSourceOfFundsType()).isEqualTo(DEFAULT_SOURCE_OF_FUNDS_TYPE);
        assertThat(testSourcesOfFundsTypeCode.getSourceOfFundsTypeDetails()).isEqualTo(DEFAULT_SOURCE_OF_FUNDS_TYPE_DETAILS);

        // Validate the SourcesOfFundsTypeCode in Elasticsearch
        verify(mockSourcesOfFundsTypeCodeSearchRepository, times(1)).save(testSourcesOfFundsTypeCode);
    }

    @Test
    @Transactional
    void createSourcesOfFundsTypeCodeWithExistingId() throws Exception {
        // Create the SourcesOfFundsTypeCode with an existing ID
        sourcesOfFundsTypeCode.setId(1L);
        SourcesOfFundsTypeCodeDTO sourcesOfFundsTypeCodeDTO = sourcesOfFundsTypeCodeMapper.toDto(sourcesOfFundsTypeCode);

        int databaseSizeBeforeCreate = sourcesOfFundsTypeCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourcesOfFundsTypeCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourcesOfFundsTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourcesOfFundsTypeCode in the database
        List<SourcesOfFundsTypeCode> sourcesOfFundsTypeCodeList = sourcesOfFundsTypeCodeRepository.findAll();
        assertThat(sourcesOfFundsTypeCodeList).hasSize(databaseSizeBeforeCreate);

        // Validate the SourcesOfFundsTypeCode in Elasticsearch
        verify(mockSourcesOfFundsTypeCodeSearchRepository, times(0)).save(sourcesOfFundsTypeCode);
    }

    @Test
    @Transactional
    void checkSourceOfFundsTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourcesOfFundsTypeCodeRepository.findAll().size();
        // set the field null
        sourcesOfFundsTypeCode.setSourceOfFundsTypeCode(null);

        // Create the SourcesOfFundsTypeCode, which fails.
        SourcesOfFundsTypeCodeDTO sourcesOfFundsTypeCodeDTO = sourcesOfFundsTypeCodeMapper.toDto(sourcesOfFundsTypeCode);

        restSourcesOfFundsTypeCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourcesOfFundsTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<SourcesOfFundsTypeCode> sourcesOfFundsTypeCodeList = sourcesOfFundsTypeCodeRepository.findAll();
        assertThat(sourcesOfFundsTypeCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSourceOfFundsTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourcesOfFundsTypeCodeRepository.findAll().size();
        // set the field null
        sourcesOfFundsTypeCode.setSourceOfFundsType(null);

        // Create the SourcesOfFundsTypeCode, which fails.
        SourcesOfFundsTypeCodeDTO sourcesOfFundsTypeCodeDTO = sourcesOfFundsTypeCodeMapper.toDto(sourcesOfFundsTypeCode);

        restSourcesOfFundsTypeCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourcesOfFundsTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<SourcesOfFundsTypeCode> sourcesOfFundsTypeCodeList = sourcesOfFundsTypeCodeRepository.findAll();
        assertThat(sourcesOfFundsTypeCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSourcesOfFundsTypeCodes() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        // Get all the sourcesOfFundsTypeCodeList
        restSourcesOfFundsTypeCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourcesOfFundsTypeCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceOfFundsTypeCode").value(hasItem(DEFAULT_SOURCE_OF_FUNDS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].sourceOfFundsType").value(hasItem(DEFAULT_SOURCE_OF_FUNDS_TYPE)))
            .andExpect(jsonPath("$.[*].sourceOfFundsTypeDetails").value(hasItem(DEFAULT_SOURCE_OF_FUNDS_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getSourcesOfFundsTypeCode() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        // Get the sourcesOfFundsTypeCode
        restSourcesOfFundsTypeCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, sourcesOfFundsTypeCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sourcesOfFundsTypeCode.getId().intValue()))
            .andExpect(jsonPath("$.sourceOfFundsTypeCode").value(DEFAULT_SOURCE_OF_FUNDS_TYPE_CODE))
            .andExpect(jsonPath("$.sourceOfFundsType").value(DEFAULT_SOURCE_OF_FUNDS_TYPE))
            .andExpect(jsonPath("$.sourceOfFundsTypeDetails").value(DEFAULT_SOURCE_OF_FUNDS_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getSourcesOfFundsTypeCodesByIdFiltering() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        Long id = sourcesOfFundsTypeCode.getId();

        defaultSourcesOfFundsTypeCodeShouldBeFound("id.equals=" + id);
        defaultSourcesOfFundsTypeCodeShouldNotBeFound("id.notEquals=" + id);

        defaultSourcesOfFundsTypeCodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSourcesOfFundsTypeCodeShouldNotBeFound("id.greaterThan=" + id);

        defaultSourcesOfFundsTypeCodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSourcesOfFundsTypeCodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSourcesOfFundsTypeCodesBySourceOfFundsTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsTypeCode equals to DEFAULT_SOURCE_OF_FUNDS_TYPE_CODE
        defaultSourcesOfFundsTypeCodeShouldBeFound("sourceOfFundsTypeCode.equals=" + DEFAULT_SOURCE_OF_FUNDS_TYPE_CODE);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsTypeCode equals to UPDATED_SOURCE_OF_FUNDS_TYPE_CODE
        defaultSourcesOfFundsTypeCodeShouldNotBeFound("sourceOfFundsTypeCode.equals=" + UPDATED_SOURCE_OF_FUNDS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSourcesOfFundsTypeCodesBySourceOfFundsTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsTypeCode not equals to DEFAULT_SOURCE_OF_FUNDS_TYPE_CODE
        defaultSourcesOfFundsTypeCodeShouldNotBeFound("sourceOfFundsTypeCode.notEquals=" + DEFAULT_SOURCE_OF_FUNDS_TYPE_CODE);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsTypeCode not equals to UPDATED_SOURCE_OF_FUNDS_TYPE_CODE
        defaultSourcesOfFundsTypeCodeShouldBeFound("sourceOfFundsTypeCode.notEquals=" + UPDATED_SOURCE_OF_FUNDS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSourcesOfFundsTypeCodesBySourceOfFundsTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsTypeCode in DEFAULT_SOURCE_OF_FUNDS_TYPE_CODE or UPDATED_SOURCE_OF_FUNDS_TYPE_CODE
        defaultSourcesOfFundsTypeCodeShouldBeFound(
            "sourceOfFundsTypeCode.in=" + DEFAULT_SOURCE_OF_FUNDS_TYPE_CODE + "," + UPDATED_SOURCE_OF_FUNDS_TYPE_CODE
        );

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsTypeCode equals to UPDATED_SOURCE_OF_FUNDS_TYPE_CODE
        defaultSourcesOfFundsTypeCodeShouldNotBeFound("sourceOfFundsTypeCode.in=" + UPDATED_SOURCE_OF_FUNDS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSourcesOfFundsTypeCodesBySourceOfFundsTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsTypeCode is not null
        defaultSourcesOfFundsTypeCodeShouldBeFound("sourceOfFundsTypeCode.specified=true");

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsTypeCode is null
        defaultSourcesOfFundsTypeCodeShouldNotBeFound("sourceOfFundsTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSourcesOfFundsTypeCodesBySourceOfFundsTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsTypeCode contains DEFAULT_SOURCE_OF_FUNDS_TYPE_CODE
        defaultSourcesOfFundsTypeCodeShouldBeFound("sourceOfFundsTypeCode.contains=" + DEFAULT_SOURCE_OF_FUNDS_TYPE_CODE);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsTypeCode contains UPDATED_SOURCE_OF_FUNDS_TYPE_CODE
        defaultSourcesOfFundsTypeCodeShouldNotBeFound("sourceOfFundsTypeCode.contains=" + UPDATED_SOURCE_OF_FUNDS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSourcesOfFundsTypeCodesBySourceOfFundsTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsTypeCode does not contain DEFAULT_SOURCE_OF_FUNDS_TYPE_CODE
        defaultSourcesOfFundsTypeCodeShouldNotBeFound("sourceOfFundsTypeCode.doesNotContain=" + DEFAULT_SOURCE_OF_FUNDS_TYPE_CODE);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsTypeCode does not contain UPDATED_SOURCE_OF_FUNDS_TYPE_CODE
        defaultSourcesOfFundsTypeCodeShouldBeFound("sourceOfFundsTypeCode.doesNotContain=" + UPDATED_SOURCE_OF_FUNDS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSourcesOfFundsTypeCodesBySourceOfFundsTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsType equals to DEFAULT_SOURCE_OF_FUNDS_TYPE
        defaultSourcesOfFundsTypeCodeShouldBeFound("sourceOfFundsType.equals=" + DEFAULT_SOURCE_OF_FUNDS_TYPE);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsType equals to UPDATED_SOURCE_OF_FUNDS_TYPE
        defaultSourcesOfFundsTypeCodeShouldNotBeFound("sourceOfFundsType.equals=" + UPDATED_SOURCE_OF_FUNDS_TYPE);
    }

    @Test
    @Transactional
    void getAllSourcesOfFundsTypeCodesBySourceOfFundsTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsType not equals to DEFAULT_SOURCE_OF_FUNDS_TYPE
        defaultSourcesOfFundsTypeCodeShouldNotBeFound("sourceOfFundsType.notEquals=" + DEFAULT_SOURCE_OF_FUNDS_TYPE);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsType not equals to UPDATED_SOURCE_OF_FUNDS_TYPE
        defaultSourcesOfFundsTypeCodeShouldBeFound("sourceOfFundsType.notEquals=" + UPDATED_SOURCE_OF_FUNDS_TYPE);
    }

    @Test
    @Transactional
    void getAllSourcesOfFundsTypeCodesBySourceOfFundsTypeIsInShouldWork() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsType in DEFAULT_SOURCE_OF_FUNDS_TYPE or UPDATED_SOURCE_OF_FUNDS_TYPE
        defaultSourcesOfFundsTypeCodeShouldBeFound(
            "sourceOfFundsType.in=" + DEFAULT_SOURCE_OF_FUNDS_TYPE + "," + UPDATED_SOURCE_OF_FUNDS_TYPE
        );

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsType equals to UPDATED_SOURCE_OF_FUNDS_TYPE
        defaultSourcesOfFundsTypeCodeShouldNotBeFound("sourceOfFundsType.in=" + UPDATED_SOURCE_OF_FUNDS_TYPE);
    }

    @Test
    @Transactional
    void getAllSourcesOfFundsTypeCodesBySourceOfFundsTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsType is not null
        defaultSourcesOfFundsTypeCodeShouldBeFound("sourceOfFundsType.specified=true");

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsType is null
        defaultSourcesOfFundsTypeCodeShouldNotBeFound("sourceOfFundsType.specified=false");
    }

    @Test
    @Transactional
    void getAllSourcesOfFundsTypeCodesBySourceOfFundsTypeContainsSomething() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsType contains DEFAULT_SOURCE_OF_FUNDS_TYPE
        defaultSourcesOfFundsTypeCodeShouldBeFound("sourceOfFundsType.contains=" + DEFAULT_SOURCE_OF_FUNDS_TYPE);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsType contains UPDATED_SOURCE_OF_FUNDS_TYPE
        defaultSourcesOfFundsTypeCodeShouldNotBeFound("sourceOfFundsType.contains=" + UPDATED_SOURCE_OF_FUNDS_TYPE);
    }

    @Test
    @Transactional
    void getAllSourcesOfFundsTypeCodesBySourceOfFundsTypeNotContainsSomething() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsType does not contain DEFAULT_SOURCE_OF_FUNDS_TYPE
        defaultSourcesOfFundsTypeCodeShouldNotBeFound("sourceOfFundsType.doesNotContain=" + DEFAULT_SOURCE_OF_FUNDS_TYPE);

        // Get all the sourcesOfFundsTypeCodeList where sourceOfFundsType does not contain UPDATED_SOURCE_OF_FUNDS_TYPE
        defaultSourcesOfFundsTypeCodeShouldBeFound("sourceOfFundsType.doesNotContain=" + UPDATED_SOURCE_OF_FUNDS_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSourcesOfFundsTypeCodeShouldBeFound(String filter) throws Exception {
        restSourcesOfFundsTypeCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourcesOfFundsTypeCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceOfFundsTypeCode").value(hasItem(DEFAULT_SOURCE_OF_FUNDS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].sourceOfFundsType").value(hasItem(DEFAULT_SOURCE_OF_FUNDS_TYPE)))
            .andExpect(jsonPath("$.[*].sourceOfFundsTypeDetails").value(hasItem(DEFAULT_SOURCE_OF_FUNDS_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restSourcesOfFundsTypeCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSourcesOfFundsTypeCodeShouldNotBeFound(String filter) throws Exception {
        restSourcesOfFundsTypeCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSourcesOfFundsTypeCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSourcesOfFundsTypeCode() throws Exception {
        // Get the sourcesOfFundsTypeCode
        restSourcesOfFundsTypeCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSourcesOfFundsTypeCode() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        int databaseSizeBeforeUpdate = sourcesOfFundsTypeCodeRepository.findAll().size();

        // Update the sourcesOfFundsTypeCode
        SourcesOfFundsTypeCode updatedSourcesOfFundsTypeCode = sourcesOfFundsTypeCodeRepository
            .findById(sourcesOfFundsTypeCode.getId())
            .get();
        // Disconnect from session so that the updates on updatedSourcesOfFundsTypeCode are not directly saved in db
        em.detach(updatedSourcesOfFundsTypeCode);
        updatedSourcesOfFundsTypeCode
            .sourceOfFundsTypeCode(UPDATED_SOURCE_OF_FUNDS_TYPE_CODE)
            .sourceOfFundsType(UPDATED_SOURCE_OF_FUNDS_TYPE)
            .sourceOfFundsTypeDetails(UPDATED_SOURCE_OF_FUNDS_TYPE_DETAILS);
        SourcesOfFundsTypeCodeDTO sourcesOfFundsTypeCodeDTO = sourcesOfFundsTypeCodeMapper.toDto(updatedSourcesOfFundsTypeCode);

        restSourcesOfFundsTypeCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sourcesOfFundsTypeCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourcesOfFundsTypeCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the SourcesOfFundsTypeCode in the database
        List<SourcesOfFundsTypeCode> sourcesOfFundsTypeCodeList = sourcesOfFundsTypeCodeRepository.findAll();
        assertThat(sourcesOfFundsTypeCodeList).hasSize(databaseSizeBeforeUpdate);
        SourcesOfFundsTypeCode testSourcesOfFundsTypeCode = sourcesOfFundsTypeCodeList.get(sourcesOfFundsTypeCodeList.size() - 1);
        assertThat(testSourcesOfFundsTypeCode.getSourceOfFundsTypeCode()).isEqualTo(UPDATED_SOURCE_OF_FUNDS_TYPE_CODE);
        assertThat(testSourcesOfFundsTypeCode.getSourceOfFundsType()).isEqualTo(UPDATED_SOURCE_OF_FUNDS_TYPE);
        assertThat(testSourcesOfFundsTypeCode.getSourceOfFundsTypeDetails()).isEqualTo(UPDATED_SOURCE_OF_FUNDS_TYPE_DETAILS);

        // Validate the SourcesOfFundsTypeCode in Elasticsearch
        verify(mockSourcesOfFundsTypeCodeSearchRepository).save(testSourcesOfFundsTypeCode);
    }

    @Test
    @Transactional
    void putNonExistingSourcesOfFundsTypeCode() throws Exception {
        int databaseSizeBeforeUpdate = sourcesOfFundsTypeCodeRepository.findAll().size();
        sourcesOfFundsTypeCode.setId(count.incrementAndGet());

        // Create the SourcesOfFundsTypeCode
        SourcesOfFundsTypeCodeDTO sourcesOfFundsTypeCodeDTO = sourcesOfFundsTypeCodeMapper.toDto(sourcesOfFundsTypeCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourcesOfFundsTypeCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sourcesOfFundsTypeCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourcesOfFundsTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourcesOfFundsTypeCode in the database
        List<SourcesOfFundsTypeCode> sourcesOfFundsTypeCodeList = sourcesOfFundsTypeCodeRepository.findAll();
        assertThat(sourcesOfFundsTypeCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourcesOfFundsTypeCode in Elasticsearch
        verify(mockSourcesOfFundsTypeCodeSearchRepository, times(0)).save(sourcesOfFundsTypeCode);
    }

    @Test
    @Transactional
    void putWithIdMismatchSourcesOfFundsTypeCode() throws Exception {
        int databaseSizeBeforeUpdate = sourcesOfFundsTypeCodeRepository.findAll().size();
        sourcesOfFundsTypeCode.setId(count.incrementAndGet());

        // Create the SourcesOfFundsTypeCode
        SourcesOfFundsTypeCodeDTO sourcesOfFundsTypeCodeDTO = sourcesOfFundsTypeCodeMapper.toDto(sourcesOfFundsTypeCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourcesOfFundsTypeCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourcesOfFundsTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourcesOfFundsTypeCode in the database
        List<SourcesOfFundsTypeCode> sourcesOfFundsTypeCodeList = sourcesOfFundsTypeCodeRepository.findAll();
        assertThat(sourcesOfFundsTypeCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourcesOfFundsTypeCode in Elasticsearch
        verify(mockSourcesOfFundsTypeCodeSearchRepository, times(0)).save(sourcesOfFundsTypeCode);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSourcesOfFundsTypeCode() throws Exception {
        int databaseSizeBeforeUpdate = sourcesOfFundsTypeCodeRepository.findAll().size();
        sourcesOfFundsTypeCode.setId(count.incrementAndGet());

        // Create the SourcesOfFundsTypeCode
        SourcesOfFundsTypeCodeDTO sourcesOfFundsTypeCodeDTO = sourcesOfFundsTypeCodeMapper.toDto(sourcesOfFundsTypeCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourcesOfFundsTypeCodeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourcesOfFundsTypeCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SourcesOfFundsTypeCode in the database
        List<SourcesOfFundsTypeCode> sourcesOfFundsTypeCodeList = sourcesOfFundsTypeCodeRepository.findAll();
        assertThat(sourcesOfFundsTypeCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourcesOfFundsTypeCode in Elasticsearch
        verify(mockSourcesOfFundsTypeCodeSearchRepository, times(0)).save(sourcesOfFundsTypeCode);
    }

    @Test
    @Transactional
    void partialUpdateSourcesOfFundsTypeCodeWithPatch() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        int databaseSizeBeforeUpdate = sourcesOfFundsTypeCodeRepository.findAll().size();

        // Update the sourcesOfFundsTypeCode using partial update
        SourcesOfFundsTypeCode partialUpdatedSourcesOfFundsTypeCode = new SourcesOfFundsTypeCode();
        partialUpdatedSourcesOfFundsTypeCode.setId(sourcesOfFundsTypeCode.getId());

        restSourcesOfFundsTypeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSourcesOfFundsTypeCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSourcesOfFundsTypeCode))
            )
            .andExpect(status().isOk());

        // Validate the SourcesOfFundsTypeCode in the database
        List<SourcesOfFundsTypeCode> sourcesOfFundsTypeCodeList = sourcesOfFundsTypeCodeRepository.findAll();
        assertThat(sourcesOfFundsTypeCodeList).hasSize(databaseSizeBeforeUpdate);
        SourcesOfFundsTypeCode testSourcesOfFundsTypeCode = sourcesOfFundsTypeCodeList.get(sourcesOfFundsTypeCodeList.size() - 1);
        assertThat(testSourcesOfFundsTypeCode.getSourceOfFundsTypeCode()).isEqualTo(DEFAULT_SOURCE_OF_FUNDS_TYPE_CODE);
        assertThat(testSourcesOfFundsTypeCode.getSourceOfFundsType()).isEqualTo(DEFAULT_SOURCE_OF_FUNDS_TYPE);
        assertThat(testSourcesOfFundsTypeCode.getSourceOfFundsTypeDetails()).isEqualTo(DEFAULT_SOURCE_OF_FUNDS_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateSourcesOfFundsTypeCodeWithPatch() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        int databaseSizeBeforeUpdate = sourcesOfFundsTypeCodeRepository.findAll().size();

        // Update the sourcesOfFundsTypeCode using partial update
        SourcesOfFundsTypeCode partialUpdatedSourcesOfFundsTypeCode = new SourcesOfFundsTypeCode();
        partialUpdatedSourcesOfFundsTypeCode.setId(sourcesOfFundsTypeCode.getId());

        partialUpdatedSourcesOfFundsTypeCode
            .sourceOfFundsTypeCode(UPDATED_SOURCE_OF_FUNDS_TYPE_CODE)
            .sourceOfFundsType(UPDATED_SOURCE_OF_FUNDS_TYPE)
            .sourceOfFundsTypeDetails(UPDATED_SOURCE_OF_FUNDS_TYPE_DETAILS);

        restSourcesOfFundsTypeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSourcesOfFundsTypeCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSourcesOfFundsTypeCode))
            )
            .andExpect(status().isOk());

        // Validate the SourcesOfFundsTypeCode in the database
        List<SourcesOfFundsTypeCode> sourcesOfFundsTypeCodeList = sourcesOfFundsTypeCodeRepository.findAll();
        assertThat(sourcesOfFundsTypeCodeList).hasSize(databaseSizeBeforeUpdate);
        SourcesOfFundsTypeCode testSourcesOfFundsTypeCode = sourcesOfFundsTypeCodeList.get(sourcesOfFundsTypeCodeList.size() - 1);
        assertThat(testSourcesOfFundsTypeCode.getSourceOfFundsTypeCode()).isEqualTo(UPDATED_SOURCE_OF_FUNDS_TYPE_CODE);
        assertThat(testSourcesOfFundsTypeCode.getSourceOfFundsType()).isEqualTo(UPDATED_SOURCE_OF_FUNDS_TYPE);
        assertThat(testSourcesOfFundsTypeCode.getSourceOfFundsTypeDetails()).isEqualTo(UPDATED_SOURCE_OF_FUNDS_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingSourcesOfFundsTypeCode() throws Exception {
        int databaseSizeBeforeUpdate = sourcesOfFundsTypeCodeRepository.findAll().size();
        sourcesOfFundsTypeCode.setId(count.incrementAndGet());

        // Create the SourcesOfFundsTypeCode
        SourcesOfFundsTypeCodeDTO sourcesOfFundsTypeCodeDTO = sourcesOfFundsTypeCodeMapper.toDto(sourcesOfFundsTypeCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourcesOfFundsTypeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sourcesOfFundsTypeCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sourcesOfFundsTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourcesOfFundsTypeCode in the database
        List<SourcesOfFundsTypeCode> sourcesOfFundsTypeCodeList = sourcesOfFundsTypeCodeRepository.findAll();
        assertThat(sourcesOfFundsTypeCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourcesOfFundsTypeCode in Elasticsearch
        verify(mockSourcesOfFundsTypeCodeSearchRepository, times(0)).save(sourcesOfFundsTypeCode);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSourcesOfFundsTypeCode() throws Exception {
        int databaseSizeBeforeUpdate = sourcesOfFundsTypeCodeRepository.findAll().size();
        sourcesOfFundsTypeCode.setId(count.incrementAndGet());

        // Create the SourcesOfFundsTypeCode
        SourcesOfFundsTypeCodeDTO sourcesOfFundsTypeCodeDTO = sourcesOfFundsTypeCodeMapper.toDto(sourcesOfFundsTypeCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourcesOfFundsTypeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sourcesOfFundsTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourcesOfFundsTypeCode in the database
        List<SourcesOfFundsTypeCode> sourcesOfFundsTypeCodeList = sourcesOfFundsTypeCodeRepository.findAll();
        assertThat(sourcesOfFundsTypeCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourcesOfFundsTypeCode in Elasticsearch
        verify(mockSourcesOfFundsTypeCodeSearchRepository, times(0)).save(sourcesOfFundsTypeCode);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSourcesOfFundsTypeCode() throws Exception {
        int databaseSizeBeforeUpdate = sourcesOfFundsTypeCodeRepository.findAll().size();
        sourcesOfFundsTypeCode.setId(count.incrementAndGet());

        // Create the SourcesOfFundsTypeCode
        SourcesOfFundsTypeCodeDTO sourcesOfFundsTypeCodeDTO = sourcesOfFundsTypeCodeMapper.toDto(sourcesOfFundsTypeCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourcesOfFundsTypeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sourcesOfFundsTypeCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SourcesOfFundsTypeCode in the database
        List<SourcesOfFundsTypeCode> sourcesOfFundsTypeCodeList = sourcesOfFundsTypeCodeRepository.findAll();
        assertThat(sourcesOfFundsTypeCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourcesOfFundsTypeCode in Elasticsearch
        verify(mockSourcesOfFundsTypeCodeSearchRepository, times(0)).save(sourcesOfFundsTypeCode);
    }

    @Test
    @Transactional
    void deleteSourcesOfFundsTypeCode() throws Exception {
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);

        int databaseSizeBeforeDelete = sourcesOfFundsTypeCodeRepository.findAll().size();

        // Delete the sourcesOfFundsTypeCode
        restSourcesOfFundsTypeCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, sourcesOfFundsTypeCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SourcesOfFundsTypeCode> sourcesOfFundsTypeCodeList = sourcesOfFundsTypeCodeRepository.findAll();
        assertThat(sourcesOfFundsTypeCodeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SourcesOfFundsTypeCode in Elasticsearch
        verify(mockSourcesOfFundsTypeCodeSearchRepository, times(1)).deleteById(sourcesOfFundsTypeCode.getId());
    }

    @Test
    @Transactional
    void searchSourcesOfFundsTypeCode() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        sourcesOfFundsTypeCodeRepository.saveAndFlush(sourcesOfFundsTypeCode);
        when(mockSourcesOfFundsTypeCodeSearchRepository.search("id:" + sourcesOfFundsTypeCode.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(sourcesOfFundsTypeCode), PageRequest.of(0, 1), 1));

        // Search the sourcesOfFundsTypeCode
        restSourcesOfFundsTypeCodeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + sourcesOfFundsTypeCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourcesOfFundsTypeCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceOfFundsTypeCode").value(hasItem(DEFAULT_SOURCE_OF_FUNDS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].sourceOfFundsType").value(hasItem(DEFAULT_SOURCE_OF_FUNDS_TYPE)))
            .andExpect(jsonPath("$.[*].sourceOfFundsTypeDetails").value(hasItem(DEFAULT_SOURCE_OF_FUNDS_TYPE_DETAILS.toString())));
    }
}
