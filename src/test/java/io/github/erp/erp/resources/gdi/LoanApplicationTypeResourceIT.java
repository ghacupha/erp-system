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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.LoanApplicationType;
import io.github.erp.repository.LoanApplicationTypeRepository;
import io.github.erp.repository.search.LoanApplicationTypeSearchRepository;
import io.github.erp.service.dto.LoanApplicationTypeDTO;
import io.github.erp.service.mapper.LoanApplicationTypeMapper;
import io.github.erp.web.rest.LoanApplicationTypeResource;
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

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LoanApplicationTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class LoanApplicationTypeResourceIT {

    private static final String DEFAULT_LOAN_APPLICATION_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_APPLICATION_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LOAN_APPLICATION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_APPLICATION_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_LOAN_APPLICATION_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_APPLICATION_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/loan-application-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/loan-application-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LoanApplicationTypeRepository loanApplicationTypeRepository;

    @Autowired
    private LoanApplicationTypeMapper loanApplicationTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LoanApplicationTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private LoanApplicationTypeSearchRepository mockLoanApplicationTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoanApplicationTypeMockMvc;

    private LoanApplicationType loanApplicationType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanApplicationType createEntity(EntityManager em) {
        LoanApplicationType loanApplicationType = new LoanApplicationType()
            .loanApplicationTypeCode(DEFAULT_LOAN_APPLICATION_TYPE_CODE)
            .loanApplicationType(DEFAULT_LOAN_APPLICATION_TYPE)
            .loanApplicationDetails(DEFAULT_LOAN_APPLICATION_DETAILS);
        return loanApplicationType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanApplicationType createUpdatedEntity(EntityManager em) {
        LoanApplicationType loanApplicationType = new LoanApplicationType()
            .loanApplicationTypeCode(UPDATED_LOAN_APPLICATION_TYPE_CODE)
            .loanApplicationType(UPDATED_LOAN_APPLICATION_TYPE)
            .loanApplicationDetails(UPDATED_LOAN_APPLICATION_DETAILS);
        return loanApplicationType;
    }

    @BeforeEach
    public void initTest() {
        loanApplicationType = createEntity(em);
    }

    @Test
    @Transactional
    void createLoanApplicationType() throws Exception {
        int databaseSizeBeforeCreate = loanApplicationTypeRepository.findAll().size();
        // Create the LoanApplicationType
        LoanApplicationTypeDTO loanApplicationTypeDTO = loanApplicationTypeMapper.toDto(loanApplicationType);
        restLoanApplicationTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LoanApplicationType in the database
        List<LoanApplicationType> loanApplicationTypeList = loanApplicationTypeRepository.findAll();
        assertThat(loanApplicationTypeList).hasSize(databaseSizeBeforeCreate + 1);
        LoanApplicationType testLoanApplicationType = loanApplicationTypeList.get(loanApplicationTypeList.size() - 1);
        assertThat(testLoanApplicationType.getLoanApplicationTypeCode()).isEqualTo(DEFAULT_LOAN_APPLICATION_TYPE_CODE);
        assertThat(testLoanApplicationType.getLoanApplicationType()).isEqualTo(DEFAULT_LOAN_APPLICATION_TYPE);
        assertThat(testLoanApplicationType.getLoanApplicationDetails()).isEqualTo(DEFAULT_LOAN_APPLICATION_DETAILS);

        // Validate the LoanApplicationType in Elasticsearch
        verify(mockLoanApplicationTypeSearchRepository, times(1)).save(testLoanApplicationType);
    }

    @Test
    @Transactional
    void createLoanApplicationTypeWithExistingId() throws Exception {
        // Create the LoanApplicationType with an existing ID
        loanApplicationType.setId(1L);
        LoanApplicationTypeDTO loanApplicationTypeDTO = loanApplicationTypeMapper.toDto(loanApplicationType);

        int databaseSizeBeforeCreate = loanApplicationTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoanApplicationTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanApplicationType in the database
        List<LoanApplicationType> loanApplicationTypeList = loanApplicationTypeRepository.findAll();
        assertThat(loanApplicationTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the LoanApplicationType in Elasticsearch
        verify(mockLoanApplicationTypeSearchRepository, times(0)).save(loanApplicationType);
    }

    @Test
    @Transactional
    void checkLoanApplicationTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanApplicationTypeRepository.findAll().size();
        // set the field null
        loanApplicationType.setLoanApplicationTypeCode(null);

        // Create the LoanApplicationType, which fails.
        LoanApplicationTypeDTO loanApplicationTypeDTO = loanApplicationTypeMapper.toDto(loanApplicationType);

        restLoanApplicationTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanApplicationType> loanApplicationTypeList = loanApplicationTypeRepository.findAll();
        assertThat(loanApplicationTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLoanApplicationTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanApplicationTypeRepository.findAll().size();
        // set the field null
        loanApplicationType.setLoanApplicationType(null);

        // Create the LoanApplicationType, which fails.
        LoanApplicationTypeDTO loanApplicationTypeDTO = loanApplicationTypeMapper.toDto(loanApplicationType);

        restLoanApplicationTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanApplicationType> loanApplicationTypeList = loanApplicationTypeRepository.findAll();
        assertThat(loanApplicationTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLoanApplicationTypes() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        // Get all the loanApplicationTypeList
        restLoanApplicationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanApplicationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanApplicationTypeCode").value(hasItem(DEFAULT_LOAN_APPLICATION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].loanApplicationType").value(hasItem(DEFAULT_LOAN_APPLICATION_TYPE)))
            .andExpect(jsonPath("$.[*].loanApplicationDetails").value(hasItem(DEFAULT_LOAN_APPLICATION_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getLoanApplicationType() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        // Get the loanApplicationType
        restLoanApplicationTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, loanApplicationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loanApplicationType.getId().intValue()))
            .andExpect(jsonPath("$.loanApplicationTypeCode").value(DEFAULT_LOAN_APPLICATION_TYPE_CODE))
            .andExpect(jsonPath("$.loanApplicationType").value(DEFAULT_LOAN_APPLICATION_TYPE))
            .andExpect(jsonPath("$.loanApplicationDetails").value(DEFAULT_LOAN_APPLICATION_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getLoanApplicationTypesByIdFiltering() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        Long id = loanApplicationType.getId();

        defaultLoanApplicationTypeShouldBeFound("id.equals=" + id);
        defaultLoanApplicationTypeShouldNotBeFound("id.notEquals=" + id);

        defaultLoanApplicationTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLoanApplicationTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultLoanApplicationTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLoanApplicationTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLoanApplicationTypesByLoanApplicationTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        // Get all the loanApplicationTypeList where loanApplicationTypeCode equals to DEFAULT_LOAN_APPLICATION_TYPE_CODE
        defaultLoanApplicationTypeShouldBeFound("loanApplicationTypeCode.equals=" + DEFAULT_LOAN_APPLICATION_TYPE_CODE);

        // Get all the loanApplicationTypeList where loanApplicationTypeCode equals to UPDATED_LOAN_APPLICATION_TYPE_CODE
        defaultLoanApplicationTypeShouldNotBeFound("loanApplicationTypeCode.equals=" + UPDATED_LOAN_APPLICATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanApplicationTypesByLoanApplicationTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        // Get all the loanApplicationTypeList where loanApplicationTypeCode not equals to DEFAULT_LOAN_APPLICATION_TYPE_CODE
        defaultLoanApplicationTypeShouldNotBeFound("loanApplicationTypeCode.notEquals=" + DEFAULT_LOAN_APPLICATION_TYPE_CODE);

        // Get all the loanApplicationTypeList where loanApplicationTypeCode not equals to UPDATED_LOAN_APPLICATION_TYPE_CODE
        defaultLoanApplicationTypeShouldBeFound("loanApplicationTypeCode.notEquals=" + UPDATED_LOAN_APPLICATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanApplicationTypesByLoanApplicationTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        // Get all the loanApplicationTypeList where loanApplicationTypeCode in DEFAULT_LOAN_APPLICATION_TYPE_CODE or UPDATED_LOAN_APPLICATION_TYPE_CODE
        defaultLoanApplicationTypeShouldBeFound(
            "loanApplicationTypeCode.in=" + DEFAULT_LOAN_APPLICATION_TYPE_CODE + "," + UPDATED_LOAN_APPLICATION_TYPE_CODE
        );

        // Get all the loanApplicationTypeList where loanApplicationTypeCode equals to UPDATED_LOAN_APPLICATION_TYPE_CODE
        defaultLoanApplicationTypeShouldNotBeFound("loanApplicationTypeCode.in=" + UPDATED_LOAN_APPLICATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanApplicationTypesByLoanApplicationTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        // Get all the loanApplicationTypeList where loanApplicationTypeCode is not null
        defaultLoanApplicationTypeShouldBeFound("loanApplicationTypeCode.specified=true");

        // Get all the loanApplicationTypeList where loanApplicationTypeCode is null
        defaultLoanApplicationTypeShouldNotBeFound("loanApplicationTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanApplicationTypesByLoanApplicationTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        // Get all the loanApplicationTypeList where loanApplicationTypeCode contains DEFAULT_LOAN_APPLICATION_TYPE_CODE
        defaultLoanApplicationTypeShouldBeFound("loanApplicationTypeCode.contains=" + DEFAULT_LOAN_APPLICATION_TYPE_CODE);

        // Get all the loanApplicationTypeList where loanApplicationTypeCode contains UPDATED_LOAN_APPLICATION_TYPE_CODE
        defaultLoanApplicationTypeShouldNotBeFound("loanApplicationTypeCode.contains=" + UPDATED_LOAN_APPLICATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanApplicationTypesByLoanApplicationTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        // Get all the loanApplicationTypeList where loanApplicationTypeCode does not contain DEFAULT_LOAN_APPLICATION_TYPE_CODE
        defaultLoanApplicationTypeShouldNotBeFound("loanApplicationTypeCode.doesNotContain=" + DEFAULT_LOAN_APPLICATION_TYPE_CODE);

        // Get all the loanApplicationTypeList where loanApplicationTypeCode does not contain UPDATED_LOAN_APPLICATION_TYPE_CODE
        defaultLoanApplicationTypeShouldBeFound("loanApplicationTypeCode.doesNotContain=" + UPDATED_LOAN_APPLICATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllLoanApplicationTypesByLoanApplicationTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        // Get all the loanApplicationTypeList where loanApplicationType equals to DEFAULT_LOAN_APPLICATION_TYPE
        defaultLoanApplicationTypeShouldBeFound("loanApplicationType.equals=" + DEFAULT_LOAN_APPLICATION_TYPE);

        // Get all the loanApplicationTypeList where loanApplicationType equals to UPDATED_LOAN_APPLICATION_TYPE
        defaultLoanApplicationTypeShouldNotBeFound("loanApplicationType.equals=" + UPDATED_LOAN_APPLICATION_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanApplicationTypesByLoanApplicationTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        // Get all the loanApplicationTypeList where loanApplicationType not equals to DEFAULT_LOAN_APPLICATION_TYPE
        defaultLoanApplicationTypeShouldNotBeFound("loanApplicationType.notEquals=" + DEFAULT_LOAN_APPLICATION_TYPE);

        // Get all the loanApplicationTypeList where loanApplicationType not equals to UPDATED_LOAN_APPLICATION_TYPE
        defaultLoanApplicationTypeShouldBeFound("loanApplicationType.notEquals=" + UPDATED_LOAN_APPLICATION_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanApplicationTypesByLoanApplicationTypeIsInShouldWork() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        // Get all the loanApplicationTypeList where loanApplicationType in DEFAULT_LOAN_APPLICATION_TYPE or UPDATED_LOAN_APPLICATION_TYPE
        defaultLoanApplicationTypeShouldBeFound(
            "loanApplicationType.in=" + DEFAULT_LOAN_APPLICATION_TYPE + "," + UPDATED_LOAN_APPLICATION_TYPE
        );

        // Get all the loanApplicationTypeList where loanApplicationType equals to UPDATED_LOAN_APPLICATION_TYPE
        defaultLoanApplicationTypeShouldNotBeFound("loanApplicationType.in=" + UPDATED_LOAN_APPLICATION_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanApplicationTypesByLoanApplicationTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        // Get all the loanApplicationTypeList where loanApplicationType is not null
        defaultLoanApplicationTypeShouldBeFound("loanApplicationType.specified=true");

        // Get all the loanApplicationTypeList where loanApplicationType is null
        defaultLoanApplicationTypeShouldNotBeFound("loanApplicationType.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanApplicationTypesByLoanApplicationTypeContainsSomething() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        // Get all the loanApplicationTypeList where loanApplicationType contains DEFAULT_LOAN_APPLICATION_TYPE
        defaultLoanApplicationTypeShouldBeFound("loanApplicationType.contains=" + DEFAULT_LOAN_APPLICATION_TYPE);

        // Get all the loanApplicationTypeList where loanApplicationType contains UPDATED_LOAN_APPLICATION_TYPE
        defaultLoanApplicationTypeShouldNotBeFound("loanApplicationType.contains=" + UPDATED_LOAN_APPLICATION_TYPE);
    }

    @Test
    @Transactional
    void getAllLoanApplicationTypesByLoanApplicationTypeNotContainsSomething() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        // Get all the loanApplicationTypeList where loanApplicationType does not contain DEFAULT_LOAN_APPLICATION_TYPE
        defaultLoanApplicationTypeShouldNotBeFound("loanApplicationType.doesNotContain=" + DEFAULT_LOAN_APPLICATION_TYPE);

        // Get all the loanApplicationTypeList where loanApplicationType does not contain UPDATED_LOAN_APPLICATION_TYPE
        defaultLoanApplicationTypeShouldBeFound("loanApplicationType.doesNotContain=" + UPDATED_LOAN_APPLICATION_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLoanApplicationTypeShouldBeFound(String filter) throws Exception {
        restLoanApplicationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanApplicationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanApplicationTypeCode").value(hasItem(DEFAULT_LOAN_APPLICATION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].loanApplicationType").value(hasItem(DEFAULT_LOAN_APPLICATION_TYPE)))
            .andExpect(jsonPath("$.[*].loanApplicationDetails").value(hasItem(DEFAULT_LOAN_APPLICATION_DETAILS.toString())));

        // Check, that the count call also returns 1
        restLoanApplicationTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLoanApplicationTypeShouldNotBeFound(String filter) throws Exception {
        restLoanApplicationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLoanApplicationTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLoanApplicationType() throws Exception {
        // Get the loanApplicationType
        restLoanApplicationTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLoanApplicationType() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        int databaseSizeBeforeUpdate = loanApplicationTypeRepository.findAll().size();

        // Update the loanApplicationType
        LoanApplicationType updatedLoanApplicationType = loanApplicationTypeRepository.findById(loanApplicationType.getId()).get();
        // Disconnect from session so that the updates on updatedLoanApplicationType are not directly saved in db
        em.detach(updatedLoanApplicationType);
        updatedLoanApplicationType
            .loanApplicationTypeCode(UPDATED_LOAN_APPLICATION_TYPE_CODE)
            .loanApplicationType(UPDATED_LOAN_APPLICATION_TYPE)
            .loanApplicationDetails(UPDATED_LOAN_APPLICATION_DETAILS);
        LoanApplicationTypeDTO loanApplicationTypeDTO = loanApplicationTypeMapper.toDto(updatedLoanApplicationType);

        restLoanApplicationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanApplicationTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoanApplicationType in the database
        List<LoanApplicationType> loanApplicationTypeList = loanApplicationTypeRepository.findAll();
        assertThat(loanApplicationTypeList).hasSize(databaseSizeBeforeUpdate);
        LoanApplicationType testLoanApplicationType = loanApplicationTypeList.get(loanApplicationTypeList.size() - 1);
        assertThat(testLoanApplicationType.getLoanApplicationTypeCode()).isEqualTo(UPDATED_LOAN_APPLICATION_TYPE_CODE);
        assertThat(testLoanApplicationType.getLoanApplicationType()).isEqualTo(UPDATED_LOAN_APPLICATION_TYPE);
        assertThat(testLoanApplicationType.getLoanApplicationDetails()).isEqualTo(UPDATED_LOAN_APPLICATION_DETAILS);

        // Validate the LoanApplicationType in Elasticsearch
        verify(mockLoanApplicationTypeSearchRepository).save(testLoanApplicationType);
    }

    @Test
    @Transactional
    void putNonExistingLoanApplicationType() throws Exception {
        int databaseSizeBeforeUpdate = loanApplicationTypeRepository.findAll().size();
        loanApplicationType.setId(count.incrementAndGet());

        // Create the LoanApplicationType
        LoanApplicationTypeDTO loanApplicationTypeDTO = loanApplicationTypeMapper.toDto(loanApplicationType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanApplicationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanApplicationTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanApplicationType in the database
        List<LoanApplicationType> loanApplicationTypeList = loanApplicationTypeRepository.findAll();
        assertThat(loanApplicationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanApplicationType in Elasticsearch
        verify(mockLoanApplicationTypeSearchRepository, times(0)).save(loanApplicationType);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoanApplicationType() throws Exception {
        int databaseSizeBeforeUpdate = loanApplicationTypeRepository.findAll().size();
        loanApplicationType.setId(count.incrementAndGet());

        // Create the LoanApplicationType
        LoanApplicationTypeDTO loanApplicationTypeDTO = loanApplicationTypeMapper.toDto(loanApplicationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanApplicationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanApplicationType in the database
        List<LoanApplicationType> loanApplicationTypeList = loanApplicationTypeRepository.findAll();
        assertThat(loanApplicationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanApplicationType in Elasticsearch
        verify(mockLoanApplicationTypeSearchRepository, times(0)).save(loanApplicationType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoanApplicationType() throws Exception {
        int databaseSizeBeforeUpdate = loanApplicationTypeRepository.findAll().size();
        loanApplicationType.setId(count.incrementAndGet());

        // Create the LoanApplicationType
        LoanApplicationTypeDTO loanApplicationTypeDTO = loanApplicationTypeMapper.toDto(loanApplicationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanApplicationTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanApplicationType in the database
        List<LoanApplicationType> loanApplicationTypeList = loanApplicationTypeRepository.findAll();
        assertThat(loanApplicationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanApplicationType in Elasticsearch
        verify(mockLoanApplicationTypeSearchRepository, times(0)).save(loanApplicationType);
    }

    @Test
    @Transactional
    void partialUpdateLoanApplicationTypeWithPatch() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        int databaseSizeBeforeUpdate = loanApplicationTypeRepository.findAll().size();

        // Update the loanApplicationType using partial update
        LoanApplicationType partialUpdatedLoanApplicationType = new LoanApplicationType();
        partialUpdatedLoanApplicationType.setId(loanApplicationType.getId());

        partialUpdatedLoanApplicationType
            .loanApplicationTypeCode(UPDATED_LOAN_APPLICATION_TYPE_CODE)
            .loanApplicationType(UPDATED_LOAN_APPLICATION_TYPE);

        restLoanApplicationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanApplicationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanApplicationType))
            )
            .andExpect(status().isOk());

        // Validate the LoanApplicationType in the database
        List<LoanApplicationType> loanApplicationTypeList = loanApplicationTypeRepository.findAll();
        assertThat(loanApplicationTypeList).hasSize(databaseSizeBeforeUpdate);
        LoanApplicationType testLoanApplicationType = loanApplicationTypeList.get(loanApplicationTypeList.size() - 1);
        assertThat(testLoanApplicationType.getLoanApplicationTypeCode()).isEqualTo(UPDATED_LOAN_APPLICATION_TYPE_CODE);
        assertThat(testLoanApplicationType.getLoanApplicationType()).isEqualTo(UPDATED_LOAN_APPLICATION_TYPE);
        assertThat(testLoanApplicationType.getLoanApplicationDetails()).isEqualTo(DEFAULT_LOAN_APPLICATION_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateLoanApplicationTypeWithPatch() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        int databaseSizeBeforeUpdate = loanApplicationTypeRepository.findAll().size();

        // Update the loanApplicationType using partial update
        LoanApplicationType partialUpdatedLoanApplicationType = new LoanApplicationType();
        partialUpdatedLoanApplicationType.setId(loanApplicationType.getId());

        partialUpdatedLoanApplicationType
            .loanApplicationTypeCode(UPDATED_LOAN_APPLICATION_TYPE_CODE)
            .loanApplicationType(UPDATED_LOAN_APPLICATION_TYPE)
            .loanApplicationDetails(UPDATED_LOAN_APPLICATION_DETAILS);

        restLoanApplicationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanApplicationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanApplicationType))
            )
            .andExpect(status().isOk());

        // Validate the LoanApplicationType in the database
        List<LoanApplicationType> loanApplicationTypeList = loanApplicationTypeRepository.findAll();
        assertThat(loanApplicationTypeList).hasSize(databaseSizeBeforeUpdate);
        LoanApplicationType testLoanApplicationType = loanApplicationTypeList.get(loanApplicationTypeList.size() - 1);
        assertThat(testLoanApplicationType.getLoanApplicationTypeCode()).isEqualTo(UPDATED_LOAN_APPLICATION_TYPE_CODE);
        assertThat(testLoanApplicationType.getLoanApplicationType()).isEqualTo(UPDATED_LOAN_APPLICATION_TYPE);
        assertThat(testLoanApplicationType.getLoanApplicationDetails()).isEqualTo(UPDATED_LOAN_APPLICATION_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingLoanApplicationType() throws Exception {
        int databaseSizeBeforeUpdate = loanApplicationTypeRepository.findAll().size();
        loanApplicationType.setId(count.incrementAndGet());

        // Create the LoanApplicationType
        LoanApplicationTypeDTO loanApplicationTypeDTO = loanApplicationTypeMapper.toDto(loanApplicationType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanApplicationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loanApplicationTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanApplicationType in the database
        List<LoanApplicationType> loanApplicationTypeList = loanApplicationTypeRepository.findAll();
        assertThat(loanApplicationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanApplicationType in Elasticsearch
        verify(mockLoanApplicationTypeSearchRepository, times(0)).save(loanApplicationType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoanApplicationType() throws Exception {
        int databaseSizeBeforeUpdate = loanApplicationTypeRepository.findAll().size();
        loanApplicationType.setId(count.incrementAndGet());

        // Create the LoanApplicationType
        LoanApplicationTypeDTO loanApplicationTypeDTO = loanApplicationTypeMapper.toDto(loanApplicationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanApplicationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanApplicationType in the database
        List<LoanApplicationType> loanApplicationTypeList = loanApplicationTypeRepository.findAll();
        assertThat(loanApplicationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanApplicationType in Elasticsearch
        verify(mockLoanApplicationTypeSearchRepository, times(0)).save(loanApplicationType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoanApplicationType() throws Exception {
        int databaseSizeBeforeUpdate = loanApplicationTypeRepository.findAll().size();
        loanApplicationType.setId(count.incrementAndGet());

        // Create the LoanApplicationType
        LoanApplicationTypeDTO loanApplicationTypeDTO = loanApplicationTypeMapper.toDto(loanApplicationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanApplicationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanApplicationTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanApplicationType in the database
        List<LoanApplicationType> loanApplicationTypeList = loanApplicationTypeRepository.findAll();
        assertThat(loanApplicationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanApplicationType in Elasticsearch
        verify(mockLoanApplicationTypeSearchRepository, times(0)).save(loanApplicationType);
    }

    @Test
    @Transactional
    void deleteLoanApplicationType() throws Exception {
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);

        int databaseSizeBeforeDelete = loanApplicationTypeRepository.findAll().size();

        // Delete the loanApplicationType
        restLoanApplicationTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, loanApplicationType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LoanApplicationType> loanApplicationTypeList = loanApplicationTypeRepository.findAll();
        assertThat(loanApplicationTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LoanApplicationType in Elasticsearch
        verify(mockLoanApplicationTypeSearchRepository, times(1)).deleteById(loanApplicationType.getId());
    }

    @Test
    @Transactional
    void searchLoanApplicationType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        loanApplicationTypeRepository.saveAndFlush(loanApplicationType);
        when(mockLoanApplicationTypeSearchRepository.search("id:" + loanApplicationType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(loanApplicationType), PageRequest.of(0, 1), 1));

        // Search the loanApplicationType
        restLoanApplicationTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + loanApplicationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanApplicationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanApplicationTypeCode").value(hasItem(DEFAULT_LOAN_APPLICATION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].loanApplicationType").value(hasItem(DEFAULT_LOAN_APPLICATION_TYPE)))
            .andExpect(jsonPath("$.[*].loanApplicationDetails").value(hasItem(DEFAULT_LOAN_APPLICATION_DETAILS.toString())));
    }
}
