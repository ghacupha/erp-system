package io.github.erp.web.rest;

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
import io.github.erp.domain.MerchantType;
import io.github.erp.repository.MerchantTypeRepository;
import io.github.erp.repository.search.MerchantTypeSearchRepository;
import io.github.erp.service.criteria.MerchantTypeCriteria;
import io.github.erp.service.dto.MerchantTypeDTO;
import io.github.erp.service.mapper.MerchantTypeMapper;
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
 * Integration tests for the {@link MerchantTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MerchantTypeResourceIT {

    private static final String DEFAULT_MERCHANT_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MERCHANT_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MERCHANT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MERCHANT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MERCHANT_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_MERCHANT_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/merchant-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/merchant-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MerchantTypeRepository merchantTypeRepository;

    @Autowired
    private MerchantTypeMapper merchantTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.MerchantTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private MerchantTypeSearchRepository mockMerchantTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMerchantTypeMockMvc;

    private MerchantType merchantType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MerchantType createEntity(EntityManager em) {
        MerchantType merchantType = new MerchantType()
            .merchantTypeCode(DEFAULT_MERCHANT_TYPE_CODE)
            .merchantType(DEFAULT_MERCHANT_TYPE)
            .merchantTypeDetails(DEFAULT_MERCHANT_TYPE_DETAILS);
        return merchantType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MerchantType createUpdatedEntity(EntityManager em) {
        MerchantType merchantType = new MerchantType()
            .merchantTypeCode(UPDATED_MERCHANT_TYPE_CODE)
            .merchantType(UPDATED_MERCHANT_TYPE)
            .merchantTypeDetails(UPDATED_MERCHANT_TYPE_DETAILS);
        return merchantType;
    }

    @BeforeEach
    public void initTest() {
        merchantType = createEntity(em);
    }

    @Test
    @Transactional
    void createMerchantType() throws Exception {
        int databaseSizeBeforeCreate = merchantTypeRepository.findAll().size();
        // Create the MerchantType
        MerchantTypeDTO merchantTypeDTO = merchantTypeMapper.toDto(merchantType);
        restMerchantTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchantTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MerchantType in the database
        List<MerchantType> merchantTypeList = merchantTypeRepository.findAll();
        assertThat(merchantTypeList).hasSize(databaseSizeBeforeCreate + 1);
        MerchantType testMerchantType = merchantTypeList.get(merchantTypeList.size() - 1);
        assertThat(testMerchantType.getMerchantTypeCode()).isEqualTo(DEFAULT_MERCHANT_TYPE_CODE);
        assertThat(testMerchantType.getMerchantType()).isEqualTo(DEFAULT_MERCHANT_TYPE);
        assertThat(testMerchantType.getMerchantTypeDetails()).isEqualTo(DEFAULT_MERCHANT_TYPE_DETAILS);

        // Validate the MerchantType in Elasticsearch
        verify(mockMerchantTypeSearchRepository, times(1)).save(testMerchantType);
    }

    @Test
    @Transactional
    void createMerchantTypeWithExistingId() throws Exception {
        // Create the MerchantType with an existing ID
        merchantType.setId(1L);
        MerchantTypeDTO merchantTypeDTO = merchantTypeMapper.toDto(merchantType);

        int databaseSizeBeforeCreate = merchantTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMerchantTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchantTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MerchantType in the database
        List<MerchantType> merchantTypeList = merchantTypeRepository.findAll();
        assertThat(merchantTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the MerchantType in Elasticsearch
        verify(mockMerchantTypeSearchRepository, times(0)).save(merchantType);
    }

    @Test
    @Transactional
    void checkMerchantTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantTypeRepository.findAll().size();
        // set the field null
        merchantType.setMerchantTypeCode(null);

        // Create the MerchantType, which fails.
        MerchantTypeDTO merchantTypeDTO = merchantTypeMapper.toDto(merchantType);

        restMerchantTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchantTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<MerchantType> merchantTypeList = merchantTypeRepository.findAll();
        assertThat(merchantTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMerchantTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantTypeRepository.findAll().size();
        // set the field null
        merchantType.setMerchantType(null);

        // Create the MerchantType, which fails.
        MerchantTypeDTO merchantTypeDTO = merchantTypeMapper.toDto(merchantType);

        restMerchantTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchantTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<MerchantType> merchantTypeList = merchantTypeRepository.findAll();
        assertThat(merchantTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMerchantTypes() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        // Get all the merchantTypeList
        restMerchantTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(merchantType.getId().intValue())))
            .andExpect(jsonPath("$.[*].merchantTypeCode").value(hasItem(DEFAULT_MERCHANT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].merchantType").value(hasItem(DEFAULT_MERCHANT_TYPE)))
            .andExpect(jsonPath("$.[*].merchantTypeDetails").value(hasItem(DEFAULT_MERCHANT_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getMerchantType() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        // Get the merchantType
        restMerchantTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, merchantType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(merchantType.getId().intValue()))
            .andExpect(jsonPath("$.merchantTypeCode").value(DEFAULT_MERCHANT_TYPE_CODE))
            .andExpect(jsonPath("$.merchantType").value(DEFAULT_MERCHANT_TYPE))
            .andExpect(jsonPath("$.merchantTypeDetails").value(DEFAULT_MERCHANT_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getMerchantTypesByIdFiltering() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        Long id = merchantType.getId();

        defaultMerchantTypeShouldBeFound("id.equals=" + id);
        defaultMerchantTypeShouldNotBeFound("id.notEquals=" + id);

        defaultMerchantTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMerchantTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultMerchantTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMerchantTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMerchantTypesByMerchantTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        // Get all the merchantTypeList where merchantTypeCode equals to DEFAULT_MERCHANT_TYPE_CODE
        defaultMerchantTypeShouldBeFound("merchantTypeCode.equals=" + DEFAULT_MERCHANT_TYPE_CODE);

        // Get all the merchantTypeList where merchantTypeCode equals to UPDATED_MERCHANT_TYPE_CODE
        defaultMerchantTypeShouldNotBeFound("merchantTypeCode.equals=" + UPDATED_MERCHANT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllMerchantTypesByMerchantTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        // Get all the merchantTypeList where merchantTypeCode not equals to DEFAULT_MERCHANT_TYPE_CODE
        defaultMerchantTypeShouldNotBeFound("merchantTypeCode.notEquals=" + DEFAULT_MERCHANT_TYPE_CODE);

        // Get all the merchantTypeList where merchantTypeCode not equals to UPDATED_MERCHANT_TYPE_CODE
        defaultMerchantTypeShouldBeFound("merchantTypeCode.notEquals=" + UPDATED_MERCHANT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllMerchantTypesByMerchantTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        // Get all the merchantTypeList where merchantTypeCode in DEFAULT_MERCHANT_TYPE_CODE or UPDATED_MERCHANT_TYPE_CODE
        defaultMerchantTypeShouldBeFound("merchantTypeCode.in=" + DEFAULT_MERCHANT_TYPE_CODE + "," + UPDATED_MERCHANT_TYPE_CODE);

        // Get all the merchantTypeList where merchantTypeCode equals to UPDATED_MERCHANT_TYPE_CODE
        defaultMerchantTypeShouldNotBeFound("merchantTypeCode.in=" + UPDATED_MERCHANT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllMerchantTypesByMerchantTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        // Get all the merchantTypeList where merchantTypeCode is not null
        defaultMerchantTypeShouldBeFound("merchantTypeCode.specified=true");

        // Get all the merchantTypeList where merchantTypeCode is null
        defaultMerchantTypeShouldNotBeFound("merchantTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantTypesByMerchantTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        // Get all the merchantTypeList where merchantTypeCode contains DEFAULT_MERCHANT_TYPE_CODE
        defaultMerchantTypeShouldBeFound("merchantTypeCode.contains=" + DEFAULT_MERCHANT_TYPE_CODE);

        // Get all the merchantTypeList where merchantTypeCode contains UPDATED_MERCHANT_TYPE_CODE
        defaultMerchantTypeShouldNotBeFound("merchantTypeCode.contains=" + UPDATED_MERCHANT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllMerchantTypesByMerchantTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        // Get all the merchantTypeList where merchantTypeCode does not contain DEFAULT_MERCHANT_TYPE_CODE
        defaultMerchantTypeShouldNotBeFound("merchantTypeCode.doesNotContain=" + DEFAULT_MERCHANT_TYPE_CODE);

        // Get all the merchantTypeList where merchantTypeCode does not contain UPDATED_MERCHANT_TYPE_CODE
        defaultMerchantTypeShouldBeFound("merchantTypeCode.doesNotContain=" + UPDATED_MERCHANT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllMerchantTypesByMerchantTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        // Get all the merchantTypeList where merchantType equals to DEFAULT_MERCHANT_TYPE
        defaultMerchantTypeShouldBeFound("merchantType.equals=" + DEFAULT_MERCHANT_TYPE);

        // Get all the merchantTypeList where merchantType equals to UPDATED_MERCHANT_TYPE
        defaultMerchantTypeShouldNotBeFound("merchantType.equals=" + UPDATED_MERCHANT_TYPE);
    }

    @Test
    @Transactional
    void getAllMerchantTypesByMerchantTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        // Get all the merchantTypeList where merchantType not equals to DEFAULT_MERCHANT_TYPE
        defaultMerchantTypeShouldNotBeFound("merchantType.notEquals=" + DEFAULT_MERCHANT_TYPE);

        // Get all the merchantTypeList where merchantType not equals to UPDATED_MERCHANT_TYPE
        defaultMerchantTypeShouldBeFound("merchantType.notEquals=" + UPDATED_MERCHANT_TYPE);
    }

    @Test
    @Transactional
    void getAllMerchantTypesByMerchantTypeIsInShouldWork() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        // Get all the merchantTypeList where merchantType in DEFAULT_MERCHANT_TYPE or UPDATED_MERCHANT_TYPE
        defaultMerchantTypeShouldBeFound("merchantType.in=" + DEFAULT_MERCHANT_TYPE + "," + UPDATED_MERCHANT_TYPE);

        // Get all the merchantTypeList where merchantType equals to UPDATED_MERCHANT_TYPE
        defaultMerchantTypeShouldNotBeFound("merchantType.in=" + UPDATED_MERCHANT_TYPE);
    }

    @Test
    @Transactional
    void getAllMerchantTypesByMerchantTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        // Get all the merchantTypeList where merchantType is not null
        defaultMerchantTypeShouldBeFound("merchantType.specified=true");

        // Get all the merchantTypeList where merchantType is null
        defaultMerchantTypeShouldNotBeFound("merchantType.specified=false");
    }

    @Test
    @Transactional
    void getAllMerchantTypesByMerchantTypeContainsSomething() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        // Get all the merchantTypeList where merchantType contains DEFAULT_MERCHANT_TYPE
        defaultMerchantTypeShouldBeFound("merchantType.contains=" + DEFAULT_MERCHANT_TYPE);

        // Get all the merchantTypeList where merchantType contains UPDATED_MERCHANT_TYPE
        defaultMerchantTypeShouldNotBeFound("merchantType.contains=" + UPDATED_MERCHANT_TYPE);
    }

    @Test
    @Transactional
    void getAllMerchantTypesByMerchantTypeNotContainsSomething() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        // Get all the merchantTypeList where merchantType does not contain DEFAULT_MERCHANT_TYPE
        defaultMerchantTypeShouldNotBeFound("merchantType.doesNotContain=" + DEFAULT_MERCHANT_TYPE);

        // Get all the merchantTypeList where merchantType does not contain UPDATED_MERCHANT_TYPE
        defaultMerchantTypeShouldBeFound("merchantType.doesNotContain=" + UPDATED_MERCHANT_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMerchantTypeShouldBeFound(String filter) throws Exception {
        restMerchantTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(merchantType.getId().intValue())))
            .andExpect(jsonPath("$.[*].merchantTypeCode").value(hasItem(DEFAULT_MERCHANT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].merchantType").value(hasItem(DEFAULT_MERCHANT_TYPE)))
            .andExpect(jsonPath("$.[*].merchantTypeDetails").value(hasItem(DEFAULT_MERCHANT_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restMerchantTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMerchantTypeShouldNotBeFound(String filter) throws Exception {
        restMerchantTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMerchantTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMerchantType() throws Exception {
        // Get the merchantType
        restMerchantTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMerchantType() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        int databaseSizeBeforeUpdate = merchantTypeRepository.findAll().size();

        // Update the merchantType
        MerchantType updatedMerchantType = merchantTypeRepository.findById(merchantType.getId()).get();
        // Disconnect from session so that the updates on updatedMerchantType are not directly saved in db
        em.detach(updatedMerchantType);
        updatedMerchantType
            .merchantTypeCode(UPDATED_MERCHANT_TYPE_CODE)
            .merchantType(UPDATED_MERCHANT_TYPE)
            .merchantTypeDetails(UPDATED_MERCHANT_TYPE_DETAILS);
        MerchantTypeDTO merchantTypeDTO = merchantTypeMapper.toDto(updatedMerchantType);

        restMerchantTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, merchantTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(merchantTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the MerchantType in the database
        List<MerchantType> merchantTypeList = merchantTypeRepository.findAll();
        assertThat(merchantTypeList).hasSize(databaseSizeBeforeUpdate);
        MerchantType testMerchantType = merchantTypeList.get(merchantTypeList.size() - 1);
        assertThat(testMerchantType.getMerchantTypeCode()).isEqualTo(UPDATED_MERCHANT_TYPE_CODE);
        assertThat(testMerchantType.getMerchantType()).isEqualTo(UPDATED_MERCHANT_TYPE);
        assertThat(testMerchantType.getMerchantTypeDetails()).isEqualTo(UPDATED_MERCHANT_TYPE_DETAILS);

        // Validate the MerchantType in Elasticsearch
        verify(mockMerchantTypeSearchRepository).save(testMerchantType);
    }

    @Test
    @Transactional
    void putNonExistingMerchantType() throws Exception {
        int databaseSizeBeforeUpdate = merchantTypeRepository.findAll().size();
        merchantType.setId(count.incrementAndGet());

        // Create the MerchantType
        MerchantTypeDTO merchantTypeDTO = merchantTypeMapper.toDto(merchantType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMerchantTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, merchantTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(merchantTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MerchantType in the database
        List<MerchantType> merchantTypeList = merchantTypeRepository.findAll();
        assertThat(merchantTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MerchantType in Elasticsearch
        verify(mockMerchantTypeSearchRepository, times(0)).save(merchantType);
    }

    @Test
    @Transactional
    void putWithIdMismatchMerchantType() throws Exception {
        int databaseSizeBeforeUpdate = merchantTypeRepository.findAll().size();
        merchantType.setId(count.incrementAndGet());

        // Create the MerchantType
        MerchantTypeDTO merchantTypeDTO = merchantTypeMapper.toDto(merchantType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(merchantTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MerchantType in the database
        List<MerchantType> merchantTypeList = merchantTypeRepository.findAll();
        assertThat(merchantTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MerchantType in Elasticsearch
        verify(mockMerchantTypeSearchRepository, times(0)).save(merchantType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMerchantType() throws Exception {
        int databaseSizeBeforeUpdate = merchantTypeRepository.findAll().size();
        merchantType.setId(count.incrementAndGet());

        // Create the MerchantType
        MerchantTypeDTO merchantTypeDTO = merchantTypeMapper.toDto(merchantType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchantTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MerchantType in the database
        List<MerchantType> merchantTypeList = merchantTypeRepository.findAll();
        assertThat(merchantTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MerchantType in Elasticsearch
        verify(mockMerchantTypeSearchRepository, times(0)).save(merchantType);
    }

    @Test
    @Transactional
    void partialUpdateMerchantTypeWithPatch() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        int databaseSizeBeforeUpdate = merchantTypeRepository.findAll().size();

        // Update the merchantType using partial update
        MerchantType partialUpdatedMerchantType = new MerchantType();
        partialUpdatedMerchantType.setId(merchantType.getId());

        partialUpdatedMerchantType.merchantTypeCode(UPDATED_MERCHANT_TYPE_CODE);

        restMerchantTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMerchantType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMerchantType))
            )
            .andExpect(status().isOk());

        // Validate the MerchantType in the database
        List<MerchantType> merchantTypeList = merchantTypeRepository.findAll();
        assertThat(merchantTypeList).hasSize(databaseSizeBeforeUpdate);
        MerchantType testMerchantType = merchantTypeList.get(merchantTypeList.size() - 1);
        assertThat(testMerchantType.getMerchantTypeCode()).isEqualTo(UPDATED_MERCHANT_TYPE_CODE);
        assertThat(testMerchantType.getMerchantType()).isEqualTo(DEFAULT_MERCHANT_TYPE);
        assertThat(testMerchantType.getMerchantTypeDetails()).isEqualTo(DEFAULT_MERCHANT_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateMerchantTypeWithPatch() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        int databaseSizeBeforeUpdate = merchantTypeRepository.findAll().size();

        // Update the merchantType using partial update
        MerchantType partialUpdatedMerchantType = new MerchantType();
        partialUpdatedMerchantType.setId(merchantType.getId());

        partialUpdatedMerchantType
            .merchantTypeCode(UPDATED_MERCHANT_TYPE_CODE)
            .merchantType(UPDATED_MERCHANT_TYPE)
            .merchantTypeDetails(UPDATED_MERCHANT_TYPE_DETAILS);

        restMerchantTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMerchantType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMerchantType))
            )
            .andExpect(status().isOk());

        // Validate the MerchantType in the database
        List<MerchantType> merchantTypeList = merchantTypeRepository.findAll();
        assertThat(merchantTypeList).hasSize(databaseSizeBeforeUpdate);
        MerchantType testMerchantType = merchantTypeList.get(merchantTypeList.size() - 1);
        assertThat(testMerchantType.getMerchantTypeCode()).isEqualTo(UPDATED_MERCHANT_TYPE_CODE);
        assertThat(testMerchantType.getMerchantType()).isEqualTo(UPDATED_MERCHANT_TYPE);
        assertThat(testMerchantType.getMerchantTypeDetails()).isEqualTo(UPDATED_MERCHANT_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingMerchantType() throws Exception {
        int databaseSizeBeforeUpdate = merchantTypeRepository.findAll().size();
        merchantType.setId(count.incrementAndGet());

        // Create the MerchantType
        MerchantTypeDTO merchantTypeDTO = merchantTypeMapper.toDto(merchantType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMerchantTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, merchantTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(merchantTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MerchantType in the database
        List<MerchantType> merchantTypeList = merchantTypeRepository.findAll();
        assertThat(merchantTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MerchantType in Elasticsearch
        verify(mockMerchantTypeSearchRepository, times(0)).save(merchantType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMerchantType() throws Exception {
        int databaseSizeBeforeUpdate = merchantTypeRepository.findAll().size();
        merchantType.setId(count.incrementAndGet());

        // Create the MerchantType
        MerchantTypeDTO merchantTypeDTO = merchantTypeMapper.toDto(merchantType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(merchantTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MerchantType in the database
        List<MerchantType> merchantTypeList = merchantTypeRepository.findAll();
        assertThat(merchantTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MerchantType in Elasticsearch
        verify(mockMerchantTypeSearchRepository, times(0)).save(merchantType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMerchantType() throws Exception {
        int databaseSizeBeforeUpdate = merchantTypeRepository.findAll().size();
        merchantType.setId(count.incrementAndGet());

        // Create the MerchantType
        MerchantTypeDTO merchantTypeDTO = merchantTypeMapper.toDto(merchantType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(merchantTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MerchantType in the database
        List<MerchantType> merchantTypeList = merchantTypeRepository.findAll();
        assertThat(merchantTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MerchantType in Elasticsearch
        verify(mockMerchantTypeSearchRepository, times(0)).save(merchantType);
    }

    @Test
    @Transactional
    void deleteMerchantType() throws Exception {
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);

        int databaseSizeBeforeDelete = merchantTypeRepository.findAll().size();

        // Delete the merchantType
        restMerchantTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, merchantType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MerchantType> merchantTypeList = merchantTypeRepository.findAll();
        assertThat(merchantTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MerchantType in Elasticsearch
        verify(mockMerchantTypeSearchRepository, times(1)).deleteById(merchantType.getId());
    }

    @Test
    @Transactional
    void searchMerchantType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        merchantTypeRepository.saveAndFlush(merchantType);
        when(mockMerchantTypeSearchRepository.search("id:" + merchantType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(merchantType), PageRequest.of(0, 1), 1));

        // Search the merchantType
        restMerchantTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + merchantType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(merchantType.getId().intValue())))
            .andExpect(jsonPath("$.[*].merchantTypeCode").value(hasItem(DEFAULT_MERCHANT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].merchantType").value(hasItem(DEFAULT_MERCHANT_TYPE)))
            .andExpect(jsonPath("$.[*].merchantTypeDetails").value(hasItem(DEFAULT_MERCHANT_TYPE_DETAILS.toString())));
    }
}
