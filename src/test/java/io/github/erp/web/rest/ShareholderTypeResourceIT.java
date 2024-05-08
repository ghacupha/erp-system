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
import io.github.erp.domain.ShareholderType;
import io.github.erp.domain.enumeration.ShareHolderTypes;
import io.github.erp.repository.ShareholderTypeRepository;
import io.github.erp.repository.search.ShareholderTypeSearchRepository;
import io.github.erp.service.criteria.ShareholderTypeCriteria;
import io.github.erp.service.dto.ShareholderTypeDTO;
import io.github.erp.service.mapper.ShareholderTypeMapper;
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

/**
 * Integration tests for the {@link ShareholderTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ShareholderTypeResourceIT {

    private static final String DEFAULT_SHARE_HOLDER_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SHARE_HOLDER_TYPE_CODE = "BBBBBBBBBB";

    private static final ShareHolderTypes DEFAULT_SHARE_HOLDER_TYPE = ShareHolderTypes.INDIVIDUAL;
    private static final ShareHolderTypes UPDATED_SHARE_HOLDER_TYPE = ShareHolderTypes.PARTNERSHIP;

    private static final String ENTITY_API_URL = "/api/shareholder-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/shareholder-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShareholderTypeRepository shareholderTypeRepository;

    @Autowired
    private ShareholderTypeMapper shareholderTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ShareholderTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ShareholderTypeSearchRepository mockShareholderTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShareholderTypeMockMvc;

    private ShareholderType shareholderType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShareholderType createEntity(EntityManager em) {
        ShareholderType shareholderType = new ShareholderType()
            .shareHolderTypeCode(DEFAULT_SHARE_HOLDER_TYPE_CODE)
            .shareHolderType(DEFAULT_SHARE_HOLDER_TYPE);
        return shareholderType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShareholderType createUpdatedEntity(EntityManager em) {
        ShareholderType shareholderType = new ShareholderType()
            .shareHolderTypeCode(UPDATED_SHARE_HOLDER_TYPE_CODE)
            .shareHolderType(UPDATED_SHARE_HOLDER_TYPE);
        return shareholderType;
    }

    @BeforeEach
    public void initTest() {
        shareholderType = createEntity(em);
    }

    @Test
    @Transactional
    void createShareholderType() throws Exception {
        int databaseSizeBeforeCreate = shareholderTypeRepository.findAll().size();
        // Create the ShareholderType
        ShareholderTypeDTO shareholderTypeDTO = shareholderTypeMapper.toDto(shareholderType);
        restShareholderTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareholderTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ShareholderType in the database
        List<ShareholderType> shareholderTypeList = shareholderTypeRepository.findAll();
        assertThat(shareholderTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ShareholderType testShareholderType = shareholderTypeList.get(shareholderTypeList.size() - 1);
        assertThat(testShareholderType.getShareHolderTypeCode()).isEqualTo(DEFAULT_SHARE_HOLDER_TYPE_CODE);
        assertThat(testShareholderType.getShareHolderType()).isEqualTo(DEFAULT_SHARE_HOLDER_TYPE);

        // Validate the ShareholderType in Elasticsearch
        verify(mockShareholderTypeSearchRepository, times(1)).save(testShareholderType);
    }

    @Test
    @Transactional
    void createShareholderTypeWithExistingId() throws Exception {
        // Create the ShareholderType with an existing ID
        shareholderType.setId(1L);
        ShareholderTypeDTO shareholderTypeDTO = shareholderTypeMapper.toDto(shareholderType);

        int databaseSizeBeforeCreate = shareholderTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShareholderTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareholderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareholderType in the database
        List<ShareholderType> shareholderTypeList = shareholderTypeRepository.findAll();
        assertThat(shareholderTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the ShareholderType in Elasticsearch
        verify(mockShareholderTypeSearchRepository, times(0)).save(shareholderType);
    }

    @Test
    @Transactional
    void checkShareHolderTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = shareholderTypeRepository.findAll().size();
        // set the field null
        shareholderType.setShareHolderTypeCode(null);

        // Create the ShareholderType, which fails.
        ShareholderTypeDTO shareholderTypeDTO = shareholderTypeMapper.toDto(shareholderType);

        restShareholderTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareholderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShareholderType> shareholderTypeList = shareholderTypeRepository.findAll();
        assertThat(shareholderTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkShareHolderTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = shareholderTypeRepository.findAll().size();
        // set the field null
        shareholderType.setShareHolderType(null);

        // Create the ShareholderType, which fails.
        ShareholderTypeDTO shareholderTypeDTO = shareholderTypeMapper.toDto(shareholderType);

        restShareholderTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareholderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShareholderType> shareholderTypeList = shareholderTypeRepository.findAll();
        assertThat(shareholderTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShareholderTypes() throws Exception {
        // Initialize the database
        shareholderTypeRepository.saveAndFlush(shareholderType);

        // Get all the shareholderTypeList
        restShareholderTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shareholderType.getId().intValue())))
            .andExpect(jsonPath("$.[*].shareHolderTypeCode").value(hasItem(DEFAULT_SHARE_HOLDER_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].shareHolderType").value(hasItem(DEFAULT_SHARE_HOLDER_TYPE.toString())));
    }

    @Test
    @Transactional
    void getShareholderType() throws Exception {
        // Initialize the database
        shareholderTypeRepository.saveAndFlush(shareholderType);

        // Get the shareholderType
        restShareholderTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, shareholderType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shareholderType.getId().intValue()))
            .andExpect(jsonPath("$.shareHolderTypeCode").value(DEFAULT_SHARE_HOLDER_TYPE_CODE))
            .andExpect(jsonPath("$.shareHolderType").value(DEFAULT_SHARE_HOLDER_TYPE.toString()));
    }

    @Test
    @Transactional
    void getShareholderTypesByIdFiltering() throws Exception {
        // Initialize the database
        shareholderTypeRepository.saveAndFlush(shareholderType);

        Long id = shareholderType.getId();

        defaultShareholderTypeShouldBeFound("id.equals=" + id);
        defaultShareholderTypeShouldNotBeFound("id.notEquals=" + id);

        defaultShareholderTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultShareholderTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultShareholderTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultShareholderTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShareholderTypesByShareHolderTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        shareholderTypeRepository.saveAndFlush(shareholderType);

        // Get all the shareholderTypeList where shareHolderTypeCode equals to DEFAULT_SHARE_HOLDER_TYPE_CODE
        defaultShareholderTypeShouldBeFound("shareHolderTypeCode.equals=" + DEFAULT_SHARE_HOLDER_TYPE_CODE);

        // Get all the shareholderTypeList where shareHolderTypeCode equals to UPDATED_SHARE_HOLDER_TYPE_CODE
        defaultShareholderTypeShouldNotBeFound("shareHolderTypeCode.equals=" + UPDATED_SHARE_HOLDER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllShareholderTypesByShareHolderTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shareholderTypeRepository.saveAndFlush(shareholderType);

        // Get all the shareholderTypeList where shareHolderTypeCode not equals to DEFAULT_SHARE_HOLDER_TYPE_CODE
        defaultShareholderTypeShouldNotBeFound("shareHolderTypeCode.notEquals=" + DEFAULT_SHARE_HOLDER_TYPE_CODE);

        // Get all the shareholderTypeList where shareHolderTypeCode not equals to UPDATED_SHARE_HOLDER_TYPE_CODE
        defaultShareholderTypeShouldBeFound("shareHolderTypeCode.notEquals=" + UPDATED_SHARE_HOLDER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllShareholderTypesByShareHolderTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        shareholderTypeRepository.saveAndFlush(shareholderType);

        // Get all the shareholderTypeList where shareHolderTypeCode in DEFAULT_SHARE_HOLDER_TYPE_CODE or UPDATED_SHARE_HOLDER_TYPE_CODE
        defaultShareholderTypeShouldBeFound(
            "shareHolderTypeCode.in=" + DEFAULT_SHARE_HOLDER_TYPE_CODE + "," + UPDATED_SHARE_HOLDER_TYPE_CODE
        );

        // Get all the shareholderTypeList where shareHolderTypeCode equals to UPDATED_SHARE_HOLDER_TYPE_CODE
        defaultShareholderTypeShouldNotBeFound("shareHolderTypeCode.in=" + UPDATED_SHARE_HOLDER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllShareholderTypesByShareHolderTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        shareholderTypeRepository.saveAndFlush(shareholderType);

        // Get all the shareholderTypeList where shareHolderTypeCode is not null
        defaultShareholderTypeShouldBeFound("shareHolderTypeCode.specified=true");

        // Get all the shareholderTypeList where shareHolderTypeCode is null
        defaultShareholderTypeShouldNotBeFound("shareHolderTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllShareholderTypesByShareHolderTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        shareholderTypeRepository.saveAndFlush(shareholderType);

        // Get all the shareholderTypeList where shareHolderTypeCode contains DEFAULT_SHARE_HOLDER_TYPE_CODE
        defaultShareholderTypeShouldBeFound("shareHolderTypeCode.contains=" + DEFAULT_SHARE_HOLDER_TYPE_CODE);

        // Get all the shareholderTypeList where shareHolderTypeCode contains UPDATED_SHARE_HOLDER_TYPE_CODE
        defaultShareholderTypeShouldNotBeFound("shareHolderTypeCode.contains=" + UPDATED_SHARE_HOLDER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllShareholderTypesByShareHolderTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        shareholderTypeRepository.saveAndFlush(shareholderType);

        // Get all the shareholderTypeList where shareHolderTypeCode does not contain DEFAULT_SHARE_HOLDER_TYPE_CODE
        defaultShareholderTypeShouldNotBeFound("shareHolderTypeCode.doesNotContain=" + DEFAULT_SHARE_HOLDER_TYPE_CODE);

        // Get all the shareholderTypeList where shareHolderTypeCode does not contain UPDATED_SHARE_HOLDER_TYPE_CODE
        defaultShareholderTypeShouldBeFound("shareHolderTypeCode.doesNotContain=" + UPDATED_SHARE_HOLDER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllShareholderTypesByShareHolderTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        shareholderTypeRepository.saveAndFlush(shareholderType);

        // Get all the shareholderTypeList where shareHolderType equals to DEFAULT_SHARE_HOLDER_TYPE
        defaultShareholderTypeShouldBeFound("shareHolderType.equals=" + DEFAULT_SHARE_HOLDER_TYPE);

        // Get all the shareholderTypeList where shareHolderType equals to UPDATED_SHARE_HOLDER_TYPE
        defaultShareholderTypeShouldNotBeFound("shareHolderType.equals=" + UPDATED_SHARE_HOLDER_TYPE);
    }

    @Test
    @Transactional
    void getAllShareholderTypesByShareHolderTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shareholderTypeRepository.saveAndFlush(shareholderType);

        // Get all the shareholderTypeList where shareHolderType not equals to DEFAULT_SHARE_HOLDER_TYPE
        defaultShareholderTypeShouldNotBeFound("shareHolderType.notEquals=" + DEFAULT_SHARE_HOLDER_TYPE);

        // Get all the shareholderTypeList where shareHolderType not equals to UPDATED_SHARE_HOLDER_TYPE
        defaultShareholderTypeShouldBeFound("shareHolderType.notEquals=" + UPDATED_SHARE_HOLDER_TYPE);
    }

    @Test
    @Transactional
    void getAllShareholderTypesByShareHolderTypeIsInShouldWork() throws Exception {
        // Initialize the database
        shareholderTypeRepository.saveAndFlush(shareholderType);

        // Get all the shareholderTypeList where shareHolderType in DEFAULT_SHARE_HOLDER_TYPE or UPDATED_SHARE_HOLDER_TYPE
        defaultShareholderTypeShouldBeFound("shareHolderType.in=" + DEFAULT_SHARE_HOLDER_TYPE + "," + UPDATED_SHARE_HOLDER_TYPE);

        // Get all the shareholderTypeList where shareHolderType equals to UPDATED_SHARE_HOLDER_TYPE
        defaultShareholderTypeShouldNotBeFound("shareHolderType.in=" + UPDATED_SHARE_HOLDER_TYPE);
    }

    @Test
    @Transactional
    void getAllShareholderTypesByShareHolderTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        shareholderTypeRepository.saveAndFlush(shareholderType);

        // Get all the shareholderTypeList where shareHolderType is not null
        defaultShareholderTypeShouldBeFound("shareHolderType.specified=true");

        // Get all the shareholderTypeList where shareHolderType is null
        defaultShareholderTypeShouldNotBeFound("shareHolderType.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShareholderTypeShouldBeFound(String filter) throws Exception {
        restShareholderTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shareholderType.getId().intValue())))
            .andExpect(jsonPath("$.[*].shareHolderTypeCode").value(hasItem(DEFAULT_SHARE_HOLDER_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].shareHolderType").value(hasItem(DEFAULT_SHARE_HOLDER_TYPE.toString())));

        // Check, that the count call also returns 1
        restShareholderTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShareholderTypeShouldNotBeFound(String filter) throws Exception {
        restShareholderTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShareholderTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShareholderType() throws Exception {
        // Get the shareholderType
        restShareholderTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewShareholderType() throws Exception {
        // Initialize the database
        shareholderTypeRepository.saveAndFlush(shareholderType);

        int databaseSizeBeforeUpdate = shareholderTypeRepository.findAll().size();

        // Update the shareholderType
        ShareholderType updatedShareholderType = shareholderTypeRepository.findById(shareholderType.getId()).get();
        // Disconnect from session so that the updates on updatedShareholderType are not directly saved in db
        em.detach(updatedShareholderType);
        updatedShareholderType.shareHolderTypeCode(UPDATED_SHARE_HOLDER_TYPE_CODE).shareHolderType(UPDATED_SHARE_HOLDER_TYPE);
        ShareholderTypeDTO shareholderTypeDTO = shareholderTypeMapper.toDto(updatedShareholderType);

        restShareholderTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shareholderTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shareholderTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShareholderType in the database
        List<ShareholderType> shareholderTypeList = shareholderTypeRepository.findAll();
        assertThat(shareholderTypeList).hasSize(databaseSizeBeforeUpdate);
        ShareholderType testShareholderType = shareholderTypeList.get(shareholderTypeList.size() - 1);
        assertThat(testShareholderType.getShareHolderTypeCode()).isEqualTo(UPDATED_SHARE_HOLDER_TYPE_CODE);
        assertThat(testShareholderType.getShareHolderType()).isEqualTo(UPDATED_SHARE_HOLDER_TYPE);

        // Validate the ShareholderType in Elasticsearch
        verify(mockShareholderTypeSearchRepository).save(testShareholderType);
    }

    @Test
    @Transactional
    void putNonExistingShareholderType() throws Exception {
        int databaseSizeBeforeUpdate = shareholderTypeRepository.findAll().size();
        shareholderType.setId(count.incrementAndGet());

        // Create the ShareholderType
        ShareholderTypeDTO shareholderTypeDTO = shareholderTypeMapper.toDto(shareholderType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShareholderTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shareholderTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shareholderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareholderType in the database
        List<ShareholderType> shareholderTypeList = shareholderTypeRepository.findAll();
        assertThat(shareholderTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShareholderType in Elasticsearch
        verify(mockShareholderTypeSearchRepository, times(0)).save(shareholderType);
    }

    @Test
    @Transactional
    void putWithIdMismatchShareholderType() throws Exception {
        int databaseSizeBeforeUpdate = shareholderTypeRepository.findAll().size();
        shareholderType.setId(count.incrementAndGet());

        // Create the ShareholderType
        ShareholderTypeDTO shareholderTypeDTO = shareholderTypeMapper.toDto(shareholderType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareholderTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shareholderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareholderType in the database
        List<ShareholderType> shareholderTypeList = shareholderTypeRepository.findAll();
        assertThat(shareholderTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShareholderType in Elasticsearch
        verify(mockShareholderTypeSearchRepository, times(0)).save(shareholderType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShareholderType() throws Exception {
        int databaseSizeBeforeUpdate = shareholderTypeRepository.findAll().size();
        shareholderType.setId(count.incrementAndGet());

        // Create the ShareholderType
        ShareholderTypeDTO shareholderTypeDTO = shareholderTypeMapper.toDto(shareholderType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareholderTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareholderTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShareholderType in the database
        List<ShareholderType> shareholderTypeList = shareholderTypeRepository.findAll();
        assertThat(shareholderTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShareholderType in Elasticsearch
        verify(mockShareholderTypeSearchRepository, times(0)).save(shareholderType);
    }

    @Test
    @Transactional
    void partialUpdateShareholderTypeWithPatch() throws Exception {
        // Initialize the database
        shareholderTypeRepository.saveAndFlush(shareholderType);

        int databaseSizeBeforeUpdate = shareholderTypeRepository.findAll().size();

        // Update the shareholderType using partial update
        ShareholderType partialUpdatedShareholderType = new ShareholderType();
        partialUpdatedShareholderType.setId(shareholderType.getId());

        partialUpdatedShareholderType.shareHolderTypeCode(UPDATED_SHARE_HOLDER_TYPE_CODE);

        restShareholderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShareholderType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShareholderType))
            )
            .andExpect(status().isOk());

        // Validate the ShareholderType in the database
        List<ShareholderType> shareholderTypeList = shareholderTypeRepository.findAll();
        assertThat(shareholderTypeList).hasSize(databaseSizeBeforeUpdate);
        ShareholderType testShareholderType = shareholderTypeList.get(shareholderTypeList.size() - 1);
        assertThat(testShareholderType.getShareHolderTypeCode()).isEqualTo(UPDATED_SHARE_HOLDER_TYPE_CODE);
        assertThat(testShareholderType.getShareHolderType()).isEqualTo(DEFAULT_SHARE_HOLDER_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateShareholderTypeWithPatch() throws Exception {
        // Initialize the database
        shareholderTypeRepository.saveAndFlush(shareholderType);

        int databaseSizeBeforeUpdate = shareholderTypeRepository.findAll().size();

        // Update the shareholderType using partial update
        ShareholderType partialUpdatedShareholderType = new ShareholderType();
        partialUpdatedShareholderType.setId(shareholderType.getId());

        partialUpdatedShareholderType.shareHolderTypeCode(UPDATED_SHARE_HOLDER_TYPE_CODE).shareHolderType(UPDATED_SHARE_HOLDER_TYPE);

        restShareholderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShareholderType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShareholderType))
            )
            .andExpect(status().isOk());

        // Validate the ShareholderType in the database
        List<ShareholderType> shareholderTypeList = shareholderTypeRepository.findAll();
        assertThat(shareholderTypeList).hasSize(databaseSizeBeforeUpdate);
        ShareholderType testShareholderType = shareholderTypeList.get(shareholderTypeList.size() - 1);
        assertThat(testShareholderType.getShareHolderTypeCode()).isEqualTo(UPDATED_SHARE_HOLDER_TYPE_CODE);
        assertThat(testShareholderType.getShareHolderType()).isEqualTo(UPDATED_SHARE_HOLDER_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingShareholderType() throws Exception {
        int databaseSizeBeforeUpdate = shareholderTypeRepository.findAll().size();
        shareholderType.setId(count.incrementAndGet());

        // Create the ShareholderType
        ShareholderTypeDTO shareholderTypeDTO = shareholderTypeMapper.toDto(shareholderType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShareholderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shareholderTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shareholderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareholderType in the database
        List<ShareholderType> shareholderTypeList = shareholderTypeRepository.findAll();
        assertThat(shareholderTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShareholderType in Elasticsearch
        verify(mockShareholderTypeSearchRepository, times(0)).save(shareholderType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShareholderType() throws Exception {
        int databaseSizeBeforeUpdate = shareholderTypeRepository.findAll().size();
        shareholderType.setId(count.incrementAndGet());

        // Create the ShareholderType
        ShareholderTypeDTO shareholderTypeDTO = shareholderTypeMapper.toDto(shareholderType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareholderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shareholderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareholderType in the database
        List<ShareholderType> shareholderTypeList = shareholderTypeRepository.findAll();
        assertThat(shareholderTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShareholderType in Elasticsearch
        verify(mockShareholderTypeSearchRepository, times(0)).save(shareholderType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShareholderType() throws Exception {
        int databaseSizeBeforeUpdate = shareholderTypeRepository.findAll().size();
        shareholderType.setId(count.incrementAndGet());

        // Create the ShareholderType
        ShareholderTypeDTO shareholderTypeDTO = shareholderTypeMapper.toDto(shareholderType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareholderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shareholderTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShareholderType in the database
        List<ShareholderType> shareholderTypeList = shareholderTypeRepository.findAll();
        assertThat(shareholderTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShareholderType in Elasticsearch
        verify(mockShareholderTypeSearchRepository, times(0)).save(shareholderType);
    }

    @Test
    @Transactional
    void deleteShareholderType() throws Exception {
        // Initialize the database
        shareholderTypeRepository.saveAndFlush(shareholderType);

        int databaseSizeBeforeDelete = shareholderTypeRepository.findAll().size();

        // Delete the shareholderType
        restShareholderTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, shareholderType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShareholderType> shareholderTypeList = shareholderTypeRepository.findAll();
        assertThat(shareholderTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ShareholderType in Elasticsearch
        verify(mockShareholderTypeSearchRepository, times(1)).deleteById(shareholderType.getId());
    }

    @Test
    @Transactional
    void searchShareholderType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        shareholderTypeRepository.saveAndFlush(shareholderType);
        when(mockShareholderTypeSearchRepository.search("id:" + shareholderType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(shareholderType), PageRequest.of(0, 1), 1));

        // Search the shareholderType
        restShareholderTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + shareholderType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shareholderType.getId().intValue())))
            .andExpect(jsonPath("$.[*].shareHolderTypeCode").value(hasItem(DEFAULT_SHARE_HOLDER_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].shareHolderType").value(hasItem(DEFAULT_SHARE_HOLDER_TYPE.toString())));
    }
}
