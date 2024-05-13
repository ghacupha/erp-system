package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.CrbSubscriptionStatusTypeCode;
import io.github.erp.repository.CrbSubscriptionStatusTypeCodeRepository;
import io.github.erp.repository.search.CrbSubscriptionStatusTypeCodeSearchRepository;
import io.github.erp.service.criteria.CrbSubscriptionStatusTypeCodeCriteria;
import io.github.erp.service.dto.CrbSubscriptionStatusTypeCodeDTO;
import io.github.erp.service.mapper.CrbSubscriptionStatusTypeCodeMapper;
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
 * Integration tests for the {@link CrbSubscriptionStatusTypeCodeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CrbSubscriptionStatusTypeCodeResourceIT {

    private static final String DEFAULT_SUBSCRIPTION_STATUS_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIPTION_STATUS_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBSCRIPTION_STATUS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIPTION_STATUS_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBSCRIPTION_STATUS_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIPTION_STATUS_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crb-subscription-status-type-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/crb-subscription-status-type-codes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbSubscriptionStatusTypeCodeRepository crbSubscriptionStatusTypeCodeRepository;

    @Autowired
    private CrbSubscriptionStatusTypeCodeMapper crbSubscriptionStatusTypeCodeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbSubscriptionStatusTypeCodeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbSubscriptionStatusTypeCodeSearchRepository mockCrbSubscriptionStatusTypeCodeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbSubscriptionStatusTypeCodeMockMvc;

    private CrbSubscriptionStatusTypeCode crbSubscriptionStatusTypeCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbSubscriptionStatusTypeCode createEntity(EntityManager em) {
        CrbSubscriptionStatusTypeCode crbSubscriptionStatusTypeCode = new CrbSubscriptionStatusTypeCode()
            .subscriptionStatusTypeCode(DEFAULT_SUBSCRIPTION_STATUS_TYPE_CODE)
            .subscriptionStatusType(DEFAULT_SUBSCRIPTION_STATUS_TYPE)
            .subscriptionStatusTypeDescription(DEFAULT_SUBSCRIPTION_STATUS_TYPE_DESCRIPTION);
        return crbSubscriptionStatusTypeCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbSubscriptionStatusTypeCode createUpdatedEntity(EntityManager em) {
        CrbSubscriptionStatusTypeCode crbSubscriptionStatusTypeCode = new CrbSubscriptionStatusTypeCode()
            .subscriptionStatusTypeCode(UPDATED_SUBSCRIPTION_STATUS_TYPE_CODE)
            .subscriptionStatusType(UPDATED_SUBSCRIPTION_STATUS_TYPE)
            .subscriptionStatusTypeDescription(UPDATED_SUBSCRIPTION_STATUS_TYPE_DESCRIPTION);
        return crbSubscriptionStatusTypeCode;
    }

    @BeforeEach
    public void initTest() {
        crbSubscriptionStatusTypeCode = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbSubscriptionStatusTypeCode() throws Exception {
        int databaseSizeBeforeCreate = crbSubscriptionStatusTypeCodeRepository.findAll().size();
        // Create the CrbSubscriptionStatusTypeCode
        CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO = crbSubscriptionStatusTypeCodeMapper.toDto(
            crbSubscriptionStatusTypeCode
        );
        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSubscriptionStatusTypeCodeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbSubscriptionStatusTypeCode in the database
        List<CrbSubscriptionStatusTypeCode> crbSubscriptionStatusTypeCodeList = crbSubscriptionStatusTypeCodeRepository.findAll();
        assertThat(crbSubscriptionStatusTypeCodeList).hasSize(databaseSizeBeforeCreate + 1);
        CrbSubscriptionStatusTypeCode testCrbSubscriptionStatusTypeCode = crbSubscriptionStatusTypeCodeList.get(
            crbSubscriptionStatusTypeCodeList.size() - 1
        );
        assertThat(testCrbSubscriptionStatusTypeCode.getSubscriptionStatusTypeCode()).isEqualTo(DEFAULT_SUBSCRIPTION_STATUS_TYPE_CODE);
        assertThat(testCrbSubscriptionStatusTypeCode.getSubscriptionStatusType()).isEqualTo(DEFAULT_SUBSCRIPTION_STATUS_TYPE);
        assertThat(testCrbSubscriptionStatusTypeCode.getSubscriptionStatusTypeDescription())
            .isEqualTo(DEFAULT_SUBSCRIPTION_STATUS_TYPE_DESCRIPTION);

        // Validate the CrbSubscriptionStatusTypeCode in Elasticsearch
        verify(mockCrbSubscriptionStatusTypeCodeSearchRepository, times(1)).save(testCrbSubscriptionStatusTypeCode);
    }

    @Test
    @Transactional
    void createCrbSubscriptionStatusTypeCodeWithExistingId() throws Exception {
        // Create the CrbSubscriptionStatusTypeCode with an existing ID
        crbSubscriptionStatusTypeCode.setId(1L);
        CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO = crbSubscriptionStatusTypeCodeMapper.toDto(
            crbSubscriptionStatusTypeCode
        );

        int databaseSizeBeforeCreate = crbSubscriptionStatusTypeCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSubscriptionStatusTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbSubscriptionStatusTypeCode in the database
        List<CrbSubscriptionStatusTypeCode> crbSubscriptionStatusTypeCodeList = crbSubscriptionStatusTypeCodeRepository.findAll();
        assertThat(crbSubscriptionStatusTypeCodeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbSubscriptionStatusTypeCode in Elasticsearch
        verify(mockCrbSubscriptionStatusTypeCodeSearchRepository, times(0)).save(crbSubscriptionStatusTypeCode);
    }

    @Test
    @Transactional
    void checkSubscriptionStatusTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbSubscriptionStatusTypeCodeRepository.findAll().size();
        // set the field null
        crbSubscriptionStatusTypeCode.setSubscriptionStatusTypeCode(null);

        // Create the CrbSubscriptionStatusTypeCode, which fails.
        CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO = crbSubscriptionStatusTypeCodeMapper.toDto(
            crbSubscriptionStatusTypeCode
        );

        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSubscriptionStatusTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbSubscriptionStatusTypeCode> crbSubscriptionStatusTypeCodeList = crbSubscriptionStatusTypeCodeRepository.findAll();
        assertThat(crbSubscriptionStatusTypeCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubscriptionStatusTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbSubscriptionStatusTypeCodeRepository.findAll().size();
        // set the field null
        crbSubscriptionStatusTypeCode.setSubscriptionStatusType(null);

        // Create the CrbSubscriptionStatusTypeCode, which fails.
        CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO = crbSubscriptionStatusTypeCodeMapper.toDto(
            crbSubscriptionStatusTypeCode
        );

        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSubscriptionStatusTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbSubscriptionStatusTypeCode> crbSubscriptionStatusTypeCodeList = crbSubscriptionStatusTypeCodeRepository.findAll();
        assertThat(crbSubscriptionStatusTypeCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbSubscriptionStatusTypeCodes() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        // Get all the crbSubscriptionStatusTypeCodeList
        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbSubscriptionStatusTypeCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].subscriptionStatusTypeCode").value(hasItem(DEFAULT_SUBSCRIPTION_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].subscriptionStatusType").value(hasItem(DEFAULT_SUBSCRIPTION_STATUS_TYPE)))
            .andExpect(
                jsonPath("$.[*].subscriptionStatusTypeDescription").value(hasItem(DEFAULT_SUBSCRIPTION_STATUS_TYPE_DESCRIPTION.toString()))
            );
    }

    @Test
    @Transactional
    void getCrbSubscriptionStatusTypeCode() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        // Get the crbSubscriptionStatusTypeCode
        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, crbSubscriptionStatusTypeCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbSubscriptionStatusTypeCode.getId().intValue()))
            .andExpect(jsonPath("$.subscriptionStatusTypeCode").value(DEFAULT_SUBSCRIPTION_STATUS_TYPE_CODE))
            .andExpect(jsonPath("$.subscriptionStatusType").value(DEFAULT_SUBSCRIPTION_STATUS_TYPE))
            .andExpect(jsonPath("$.subscriptionStatusTypeDescription").value(DEFAULT_SUBSCRIPTION_STATUS_TYPE_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getCrbSubscriptionStatusTypeCodesByIdFiltering() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        Long id = crbSubscriptionStatusTypeCode.getId();

        defaultCrbSubscriptionStatusTypeCodeShouldBeFound("id.equals=" + id);
        defaultCrbSubscriptionStatusTypeCodeShouldNotBeFound("id.notEquals=" + id);

        defaultCrbSubscriptionStatusTypeCodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbSubscriptionStatusTypeCodeShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbSubscriptionStatusTypeCodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbSubscriptionStatusTypeCodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbSubscriptionStatusTypeCodesBySubscriptionStatusTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusTypeCode equals to DEFAULT_SUBSCRIPTION_STATUS_TYPE_CODE
        defaultCrbSubscriptionStatusTypeCodeShouldBeFound("subscriptionStatusTypeCode.equals=" + DEFAULT_SUBSCRIPTION_STATUS_TYPE_CODE);

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusTypeCode equals to UPDATED_SUBSCRIPTION_STATUS_TYPE_CODE
        defaultCrbSubscriptionStatusTypeCodeShouldNotBeFound("subscriptionStatusTypeCode.equals=" + UPDATED_SUBSCRIPTION_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbSubscriptionStatusTypeCodesBySubscriptionStatusTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusTypeCode not equals to DEFAULT_SUBSCRIPTION_STATUS_TYPE_CODE
        defaultCrbSubscriptionStatusTypeCodeShouldNotBeFound(
            "subscriptionStatusTypeCode.notEquals=" + DEFAULT_SUBSCRIPTION_STATUS_TYPE_CODE
        );

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusTypeCode not equals to UPDATED_SUBSCRIPTION_STATUS_TYPE_CODE
        defaultCrbSubscriptionStatusTypeCodeShouldBeFound("subscriptionStatusTypeCode.notEquals=" + UPDATED_SUBSCRIPTION_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbSubscriptionStatusTypeCodesBySubscriptionStatusTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusTypeCode in DEFAULT_SUBSCRIPTION_STATUS_TYPE_CODE or UPDATED_SUBSCRIPTION_STATUS_TYPE_CODE
        defaultCrbSubscriptionStatusTypeCodeShouldBeFound(
            "subscriptionStatusTypeCode.in=" + DEFAULT_SUBSCRIPTION_STATUS_TYPE_CODE + "," + UPDATED_SUBSCRIPTION_STATUS_TYPE_CODE
        );

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusTypeCode equals to UPDATED_SUBSCRIPTION_STATUS_TYPE_CODE
        defaultCrbSubscriptionStatusTypeCodeShouldNotBeFound("subscriptionStatusTypeCode.in=" + UPDATED_SUBSCRIPTION_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbSubscriptionStatusTypeCodesBySubscriptionStatusTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusTypeCode is not null
        defaultCrbSubscriptionStatusTypeCodeShouldBeFound("subscriptionStatusTypeCode.specified=true");

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusTypeCode is null
        defaultCrbSubscriptionStatusTypeCodeShouldNotBeFound("subscriptionStatusTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbSubscriptionStatusTypeCodesBySubscriptionStatusTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusTypeCode contains DEFAULT_SUBSCRIPTION_STATUS_TYPE_CODE
        defaultCrbSubscriptionStatusTypeCodeShouldBeFound("subscriptionStatusTypeCode.contains=" + DEFAULT_SUBSCRIPTION_STATUS_TYPE_CODE);

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusTypeCode contains UPDATED_SUBSCRIPTION_STATUS_TYPE_CODE
        defaultCrbSubscriptionStatusTypeCodeShouldNotBeFound(
            "subscriptionStatusTypeCode.contains=" + UPDATED_SUBSCRIPTION_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbSubscriptionStatusTypeCodesBySubscriptionStatusTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusTypeCode does not contain DEFAULT_SUBSCRIPTION_STATUS_TYPE_CODE
        defaultCrbSubscriptionStatusTypeCodeShouldNotBeFound(
            "subscriptionStatusTypeCode.doesNotContain=" + DEFAULT_SUBSCRIPTION_STATUS_TYPE_CODE
        );

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusTypeCode does not contain UPDATED_SUBSCRIPTION_STATUS_TYPE_CODE
        defaultCrbSubscriptionStatusTypeCodeShouldBeFound(
            "subscriptionStatusTypeCode.doesNotContain=" + UPDATED_SUBSCRIPTION_STATUS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbSubscriptionStatusTypeCodesBySubscriptionStatusTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusType equals to DEFAULT_SUBSCRIPTION_STATUS_TYPE
        defaultCrbSubscriptionStatusTypeCodeShouldBeFound("subscriptionStatusType.equals=" + DEFAULT_SUBSCRIPTION_STATUS_TYPE);

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusType equals to UPDATED_SUBSCRIPTION_STATUS_TYPE
        defaultCrbSubscriptionStatusTypeCodeShouldNotBeFound("subscriptionStatusType.equals=" + UPDATED_SUBSCRIPTION_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbSubscriptionStatusTypeCodesBySubscriptionStatusTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusType not equals to DEFAULT_SUBSCRIPTION_STATUS_TYPE
        defaultCrbSubscriptionStatusTypeCodeShouldNotBeFound("subscriptionStatusType.notEquals=" + DEFAULT_SUBSCRIPTION_STATUS_TYPE);

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusType not equals to UPDATED_SUBSCRIPTION_STATUS_TYPE
        defaultCrbSubscriptionStatusTypeCodeShouldBeFound("subscriptionStatusType.notEquals=" + UPDATED_SUBSCRIPTION_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbSubscriptionStatusTypeCodesBySubscriptionStatusTypeIsInShouldWork() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusType in DEFAULT_SUBSCRIPTION_STATUS_TYPE or UPDATED_SUBSCRIPTION_STATUS_TYPE
        defaultCrbSubscriptionStatusTypeCodeShouldBeFound(
            "subscriptionStatusType.in=" + DEFAULT_SUBSCRIPTION_STATUS_TYPE + "," + UPDATED_SUBSCRIPTION_STATUS_TYPE
        );

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusType equals to UPDATED_SUBSCRIPTION_STATUS_TYPE
        defaultCrbSubscriptionStatusTypeCodeShouldNotBeFound("subscriptionStatusType.in=" + UPDATED_SUBSCRIPTION_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbSubscriptionStatusTypeCodesBySubscriptionStatusTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusType is not null
        defaultCrbSubscriptionStatusTypeCodeShouldBeFound("subscriptionStatusType.specified=true");

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusType is null
        defaultCrbSubscriptionStatusTypeCodeShouldNotBeFound("subscriptionStatusType.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbSubscriptionStatusTypeCodesBySubscriptionStatusTypeContainsSomething() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusType contains DEFAULT_SUBSCRIPTION_STATUS_TYPE
        defaultCrbSubscriptionStatusTypeCodeShouldBeFound("subscriptionStatusType.contains=" + DEFAULT_SUBSCRIPTION_STATUS_TYPE);

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusType contains UPDATED_SUBSCRIPTION_STATUS_TYPE
        defaultCrbSubscriptionStatusTypeCodeShouldNotBeFound("subscriptionStatusType.contains=" + UPDATED_SUBSCRIPTION_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbSubscriptionStatusTypeCodesBySubscriptionStatusTypeNotContainsSomething() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusType does not contain DEFAULT_SUBSCRIPTION_STATUS_TYPE
        defaultCrbSubscriptionStatusTypeCodeShouldNotBeFound("subscriptionStatusType.doesNotContain=" + DEFAULT_SUBSCRIPTION_STATUS_TYPE);

        // Get all the crbSubscriptionStatusTypeCodeList where subscriptionStatusType does not contain UPDATED_SUBSCRIPTION_STATUS_TYPE
        defaultCrbSubscriptionStatusTypeCodeShouldBeFound("subscriptionStatusType.doesNotContain=" + UPDATED_SUBSCRIPTION_STATUS_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbSubscriptionStatusTypeCodeShouldBeFound(String filter) throws Exception {
        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbSubscriptionStatusTypeCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].subscriptionStatusTypeCode").value(hasItem(DEFAULT_SUBSCRIPTION_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].subscriptionStatusType").value(hasItem(DEFAULT_SUBSCRIPTION_STATUS_TYPE)))
            .andExpect(
                jsonPath("$.[*].subscriptionStatusTypeDescription").value(hasItem(DEFAULT_SUBSCRIPTION_STATUS_TYPE_DESCRIPTION.toString()))
            );

        // Check, that the count call also returns 1
        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbSubscriptionStatusTypeCodeShouldNotBeFound(String filter) throws Exception {
        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbSubscriptionStatusTypeCode() throws Exception {
        // Get the crbSubscriptionStatusTypeCode
        restCrbSubscriptionStatusTypeCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbSubscriptionStatusTypeCode() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        int databaseSizeBeforeUpdate = crbSubscriptionStatusTypeCodeRepository.findAll().size();

        // Update the crbSubscriptionStatusTypeCode
        CrbSubscriptionStatusTypeCode updatedCrbSubscriptionStatusTypeCode = crbSubscriptionStatusTypeCodeRepository
            .findById(crbSubscriptionStatusTypeCode.getId())
            .get();
        // Disconnect from session so that the updates on updatedCrbSubscriptionStatusTypeCode are not directly saved in db
        em.detach(updatedCrbSubscriptionStatusTypeCode);
        updatedCrbSubscriptionStatusTypeCode
            .subscriptionStatusTypeCode(UPDATED_SUBSCRIPTION_STATUS_TYPE_CODE)
            .subscriptionStatusType(UPDATED_SUBSCRIPTION_STATUS_TYPE)
            .subscriptionStatusTypeDescription(UPDATED_SUBSCRIPTION_STATUS_TYPE_DESCRIPTION);
        CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO = crbSubscriptionStatusTypeCodeMapper.toDto(
            updatedCrbSubscriptionStatusTypeCode
        );

        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbSubscriptionStatusTypeCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSubscriptionStatusTypeCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbSubscriptionStatusTypeCode in the database
        List<CrbSubscriptionStatusTypeCode> crbSubscriptionStatusTypeCodeList = crbSubscriptionStatusTypeCodeRepository.findAll();
        assertThat(crbSubscriptionStatusTypeCodeList).hasSize(databaseSizeBeforeUpdate);
        CrbSubscriptionStatusTypeCode testCrbSubscriptionStatusTypeCode = crbSubscriptionStatusTypeCodeList.get(
            crbSubscriptionStatusTypeCodeList.size() - 1
        );
        assertThat(testCrbSubscriptionStatusTypeCode.getSubscriptionStatusTypeCode()).isEqualTo(UPDATED_SUBSCRIPTION_STATUS_TYPE_CODE);
        assertThat(testCrbSubscriptionStatusTypeCode.getSubscriptionStatusType()).isEqualTo(UPDATED_SUBSCRIPTION_STATUS_TYPE);
        assertThat(testCrbSubscriptionStatusTypeCode.getSubscriptionStatusTypeDescription())
            .isEqualTo(UPDATED_SUBSCRIPTION_STATUS_TYPE_DESCRIPTION);

        // Validate the CrbSubscriptionStatusTypeCode in Elasticsearch
        verify(mockCrbSubscriptionStatusTypeCodeSearchRepository).save(testCrbSubscriptionStatusTypeCode);
    }

    @Test
    @Transactional
    void putNonExistingCrbSubscriptionStatusTypeCode() throws Exception {
        int databaseSizeBeforeUpdate = crbSubscriptionStatusTypeCodeRepository.findAll().size();
        crbSubscriptionStatusTypeCode.setId(count.incrementAndGet());

        // Create the CrbSubscriptionStatusTypeCode
        CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO = crbSubscriptionStatusTypeCodeMapper.toDto(
            crbSubscriptionStatusTypeCode
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbSubscriptionStatusTypeCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSubscriptionStatusTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbSubscriptionStatusTypeCode in the database
        List<CrbSubscriptionStatusTypeCode> crbSubscriptionStatusTypeCodeList = crbSubscriptionStatusTypeCodeRepository.findAll();
        assertThat(crbSubscriptionStatusTypeCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbSubscriptionStatusTypeCode in Elasticsearch
        verify(mockCrbSubscriptionStatusTypeCodeSearchRepository, times(0)).save(crbSubscriptionStatusTypeCode);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbSubscriptionStatusTypeCode() throws Exception {
        int databaseSizeBeforeUpdate = crbSubscriptionStatusTypeCodeRepository.findAll().size();
        crbSubscriptionStatusTypeCode.setId(count.incrementAndGet());

        // Create the CrbSubscriptionStatusTypeCode
        CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO = crbSubscriptionStatusTypeCodeMapper.toDto(
            crbSubscriptionStatusTypeCode
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSubscriptionStatusTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbSubscriptionStatusTypeCode in the database
        List<CrbSubscriptionStatusTypeCode> crbSubscriptionStatusTypeCodeList = crbSubscriptionStatusTypeCodeRepository.findAll();
        assertThat(crbSubscriptionStatusTypeCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbSubscriptionStatusTypeCode in Elasticsearch
        verify(mockCrbSubscriptionStatusTypeCodeSearchRepository, times(0)).save(crbSubscriptionStatusTypeCode);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbSubscriptionStatusTypeCode() throws Exception {
        int databaseSizeBeforeUpdate = crbSubscriptionStatusTypeCodeRepository.findAll().size();
        crbSubscriptionStatusTypeCode.setId(count.incrementAndGet());

        // Create the CrbSubscriptionStatusTypeCode
        CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO = crbSubscriptionStatusTypeCodeMapper.toDto(
            crbSubscriptionStatusTypeCode
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSubscriptionStatusTypeCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbSubscriptionStatusTypeCode in the database
        List<CrbSubscriptionStatusTypeCode> crbSubscriptionStatusTypeCodeList = crbSubscriptionStatusTypeCodeRepository.findAll();
        assertThat(crbSubscriptionStatusTypeCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbSubscriptionStatusTypeCode in Elasticsearch
        verify(mockCrbSubscriptionStatusTypeCodeSearchRepository, times(0)).save(crbSubscriptionStatusTypeCode);
    }

    @Test
    @Transactional
    void partialUpdateCrbSubscriptionStatusTypeCodeWithPatch() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        int databaseSizeBeforeUpdate = crbSubscriptionStatusTypeCodeRepository.findAll().size();

        // Update the crbSubscriptionStatusTypeCode using partial update
        CrbSubscriptionStatusTypeCode partialUpdatedCrbSubscriptionStatusTypeCode = new CrbSubscriptionStatusTypeCode();
        partialUpdatedCrbSubscriptionStatusTypeCode.setId(crbSubscriptionStatusTypeCode.getId());

        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbSubscriptionStatusTypeCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbSubscriptionStatusTypeCode))
            )
            .andExpect(status().isOk());

        // Validate the CrbSubscriptionStatusTypeCode in the database
        List<CrbSubscriptionStatusTypeCode> crbSubscriptionStatusTypeCodeList = crbSubscriptionStatusTypeCodeRepository.findAll();
        assertThat(crbSubscriptionStatusTypeCodeList).hasSize(databaseSizeBeforeUpdate);
        CrbSubscriptionStatusTypeCode testCrbSubscriptionStatusTypeCode = crbSubscriptionStatusTypeCodeList.get(
            crbSubscriptionStatusTypeCodeList.size() - 1
        );
        assertThat(testCrbSubscriptionStatusTypeCode.getSubscriptionStatusTypeCode()).isEqualTo(DEFAULT_SUBSCRIPTION_STATUS_TYPE_CODE);
        assertThat(testCrbSubscriptionStatusTypeCode.getSubscriptionStatusType()).isEqualTo(DEFAULT_SUBSCRIPTION_STATUS_TYPE);
        assertThat(testCrbSubscriptionStatusTypeCode.getSubscriptionStatusTypeDescription())
            .isEqualTo(DEFAULT_SUBSCRIPTION_STATUS_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCrbSubscriptionStatusTypeCodeWithPatch() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        int databaseSizeBeforeUpdate = crbSubscriptionStatusTypeCodeRepository.findAll().size();

        // Update the crbSubscriptionStatusTypeCode using partial update
        CrbSubscriptionStatusTypeCode partialUpdatedCrbSubscriptionStatusTypeCode = new CrbSubscriptionStatusTypeCode();
        partialUpdatedCrbSubscriptionStatusTypeCode.setId(crbSubscriptionStatusTypeCode.getId());

        partialUpdatedCrbSubscriptionStatusTypeCode
            .subscriptionStatusTypeCode(UPDATED_SUBSCRIPTION_STATUS_TYPE_CODE)
            .subscriptionStatusType(UPDATED_SUBSCRIPTION_STATUS_TYPE)
            .subscriptionStatusTypeDescription(UPDATED_SUBSCRIPTION_STATUS_TYPE_DESCRIPTION);

        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbSubscriptionStatusTypeCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbSubscriptionStatusTypeCode))
            )
            .andExpect(status().isOk());

        // Validate the CrbSubscriptionStatusTypeCode in the database
        List<CrbSubscriptionStatusTypeCode> crbSubscriptionStatusTypeCodeList = crbSubscriptionStatusTypeCodeRepository.findAll();
        assertThat(crbSubscriptionStatusTypeCodeList).hasSize(databaseSizeBeforeUpdate);
        CrbSubscriptionStatusTypeCode testCrbSubscriptionStatusTypeCode = crbSubscriptionStatusTypeCodeList.get(
            crbSubscriptionStatusTypeCodeList.size() - 1
        );
        assertThat(testCrbSubscriptionStatusTypeCode.getSubscriptionStatusTypeCode()).isEqualTo(UPDATED_SUBSCRIPTION_STATUS_TYPE_CODE);
        assertThat(testCrbSubscriptionStatusTypeCode.getSubscriptionStatusType()).isEqualTo(UPDATED_SUBSCRIPTION_STATUS_TYPE);
        assertThat(testCrbSubscriptionStatusTypeCode.getSubscriptionStatusTypeDescription())
            .isEqualTo(UPDATED_SUBSCRIPTION_STATUS_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCrbSubscriptionStatusTypeCode() throws Exception {
        int databaseSizeBeforeUpdate = crbSubscriptionStatusTypeCodeRepository.findAll().size();
        crbSubscriptionStatusTypeCode.setId(count.incrementAndGet());

        // Create the CrbSubscriptionStatusTypeCode
        CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO = crbSubscriptionStatusTypeCodeMapper.toDto(
            crbSubscriptionStatusTypeCode
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbSubscriptionStatusTypeCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbSubscriptionStatusTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbSubscriptionStatusTypeCode in the database
        List<CrbSubscriptionStatusTypeCode> crbSubscriptionStatusTypeCodeList = crbSubscriptionStatusTypeCodeRepository.findAll();
        assertThat(crbSubscriptionStatusTypeCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbSubscriptionStatusTypeCode in Elasticsearch
        verify(mockCrbSubscriptionStatusTypeCodeSearchRepository, times(0)).save(crbSubscriptionStatusTypeCode);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbSubscriptionStatusTypeCode() throws Exception {
        int databaseSizeBeforeUpdate = crbSubscriptionStatusTypeCodeRepository.findAll().size();
        crbSubscriptionStatusTypeCode.setId(count.incrementAndGet());

        // Create the CrbSubscriptionStatusTypeCode
        CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO = crbSubscriptionStatusTypeCodeMapper.toDto(
            crbSubscriptionStatusTypeCode
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbSubscriptionStatusTypeCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbSubscriptionStatusTypeCode in the database
        List<CrbSubscriptionStatusTypeCode> crbSubscriptionStatusTypeCodeList = crbSubscriptionStatusTypeCodeRepository.findAll();
        assertThat(crbSubscriptionStatusTypeCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbSubscriptionStatusTypeCode in Elasticsearch
        verify(mockCrbSubscriptionStatusTypeCodeSearchRepository, times(0)).save(crbSubscriptionStatusTypeCode);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbSubscriptionStatusTypeCode() throws Exception {
        int databaseSizeBeforeUpdate = crbSubscriptionStatusTypeCodeRepository.findAll().size();
        crbSubscriptionStatusTypeCode.setId(count.incrementAndGet());

        // Create the CrbSubscriptionStatusTypeCode
        CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO = crbSubscriptionStatusTypeCodeMapper.toDto(
            crbSubscriptionStatusTypeCode
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbSubscriptionStatusTypeCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbSubscriptionStatusTypeCode in the database
        List<CrbSubscriptionStatusTypeCode> crbSubscriptionStatusTypeCodeList = crbSubscriptionStatusTypeCodeRepository.findAll();
        assertThat(crbSubscriptionStatusTypeCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbSubscriptionStatusTypeCode in Elasticsearch
        verify(mockCrbSubscriptionStatusTypeCodeSearchRepository, times(0)).save(crbSubscriptionStatusTypeCode);
    }

    @Test
    @Transactional
    void deleteCrbSubscriptionStatusTypeCode() throws Exception {
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);

        int databaseSizeBeforeDelete = crbSubscriptionStatusTypeCodeRepository.findAll().size();

        // Delete the crbSubscriptionStatusTypeCode
        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbSubscriptionStatusTypeCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbSubscriptionStatusTypeCode> crbSubscriptionStatusTypeCodeList = crbSubscriptionStatusTypeCodeRepository.findAll();
        assertThat(crbSubscriptionStatusTypeCodeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbSubscriptionStatusTypeCode in Elasticsearch
        verify(mockCrbSubscriptionStatusTypeCodeSearchRepository, times(1)).deleteById(crbSubscriptionStatusTypeCode.getId());
    }

    @Test
    @Transactional
    void searchCrbSubscriptionStatusTypeCode() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbSubscriptionStatusTypeCodeRepository.saveAndFlush(crbSubscriptionStatusTypeCode);
        when(mockCrbSubscriptionStatusTypeCodeSearchRepository.search("id:" + crbSubscriptionStatusTypeCode.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbSubscriptionStatusTypeCode), PageRequest.of(0, 1), 1));

        // Search the crbSubscriptionStatusTypeCode
        restCrbSubscriptionStatusTypeCodeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbSubscriptionStatusTypeCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbSubscriptionStatusTypeCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].subscriptionStatusTypeCode").value(hasItem(DEFAULT_SUBSCRIPTION_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].subscriptionStatusType").value(hasItem(DEFAULT_SUBSCRIPTION_STATUS_TYPE)))
            .andExpect(
                jsonPath("$.[*].subscriptionStatusTypeDescription").value(hasItem(DEFAULT_SUBSCRIPTION_STATUS_TYPE_DESCRIPTION.toString()))
            );
    }
}
