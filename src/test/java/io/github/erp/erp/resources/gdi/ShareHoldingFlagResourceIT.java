package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
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
import io.github.erp.domain.ShareHoldingFlag;
import io.github.erp.domain.enumeration.ShareholdingFlagTypes;
import io.github.erp.repository.ShareHoldingFlagRepository;
import io.github.erp.repository.search.ShareHoldingFlagSearchRepository;
import io.github.erp.service.dto.ShareHoldingFlagDTO;
import io.github.erp.service.mapper.ShareHoldingFlagMapper;
import io.github.erp.web.rest.ShareHoldingFlagResource;
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
 * Integration tests for the {@link ShareHoldingFlagResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class ShareHoldingFlagResourceIT {

    private static final ShareholdingFlagTypes DEFAULT_SHAREHOLDING_FLAG_TYPE_CODE = ShareholdingFlagTypes.Y;
    private static final ShareholdingFlagTypes UPDATED_SHAREHOLDING_FLAG_TYPE_CODE = ShareholdingFlagTypes.N;

    private static final String DEFAULT_SHAREHOLDING_FLAG_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SHAREHOLDING_FLAG_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SHAREHOLDING_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SHAREHOLDING_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/share-holding-flags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/share-holding-flags";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShareHoldingFlagRepository shareHoldingFlagRepository;

    @Autowired
    private ShareHoldingFlagMapper shareHoldingFlagMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ShareHoldingFlagSearchRepositoryMockConfiguration
     */
    @Autowired
    private ShareHoldingFlagSearchRepository mockShareHoldingFlagSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShareHoldingFlagMockMvc;

    private ShareHoldingFlag shareHoldingFlag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShareHoldingFlag createEntity(EntityManager em) {
        ShareHoldingFlag shareHoldingFlag = new ShareHoldingFlag()
            .shareholdingFlagTypeCode(DEFAULT_SHAREHOLDING_FLAG_TYPE_CODE)
            .shareholdingFlagType(DEFAULT_SHAREHOLDING_FLAG_TYPE)
            .shareholdingTypeDescription(DEFAULT_SHAREHOLDING_TYPE_DESCRIPTION);
        return shareHoldingFlag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShareHoldingFlag createUpdatedEntity(EntityManager em) {
        ShareHoldingFlag shareHoldingFlag = new ShareHoldingFlag()
            .shareholdingFlagTypeCode(UPDATED_SHAREHOLDING_FLAG_TYPE_CODE)
            .shareholdingFlagType(UPDATED_SHAREHOLDING_FLAG_TYPE)
            .shareholdingTypeDescription(UPDATED_SHAREHOLDING_TYPE_DESCRIPTION);
        return shareHoldingFlag;
    }

    @BeforeEach
    public void initTest() {
        shareHoldingFlag = createEntity(em);
    }

    @Test
    @Transactional
    void createShareHoldingFlag() throws Exception {
        int databaseSizeBeforeCreate = shareHoldingFlagRepository.findAll().size();
        // Create the ShareHoldingFlag
        ShareHoldingFlagDTO shareHoldingFlagDTO = shareHoldingFlagMapper.toDto(shareHoldingFlag);
        restShareHoldingFlagMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareHoldingFlagDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ShareHoldingFlag in the database
        List<ShareHoldingFlag> shareHoldingFlagList = shareHoldingFlagRepository.findAll();
        assertThat(shareHoldingFlagList).hasSize(databaseSizeBeforeCreate + 1);
        ShareHoldingFlag testShareHoldingFlag = shareHoldingFlagList.get(shareHoldingFlagList.size() - 1);
        assertThat(testShareHoldingFlag.getShareholdingFlagTypeCode()).isEqualTo(DEFAULT_SHAREHOLDING_FLAG_TYPE_CODE);
        assertThat(testShareHoldingFlag.getShareholdingFlagType()).isEqualTo(DEFAULT_SHAREHOLDING_FLAG_TYPE);
        assertThat(testShareHoldingFlag.getShareholdingTypeDescription()).isEqualTo(DEFAULT_SHAREHOLDING_TYPE_DESCRIPTION);

        // Validate the ShareHoldingFlag in Elasticsearch
        verify(mockShareHoldingFlagSearchRepository, times(1)).save(testShareHoldingFlag);
    }

    @Test
    @Transactional
    void createShareHoldingFlagWithExistingId() throws Exception {
        // Create the ShareHoldingFlag with an existing ID
        shareHoldingFlag.setId(1L);
        ShareHoldingFlagDTO shareHoldingFlagDTO = shareHoldingFlagMapper.toDto(shareHoldingFlag);

        int databaseSizeBeforeCreate = shareHoldingFlagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShareHoldingFlagMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareHoldingFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareHoldingFlag in the database
        List<ShareHoldingFlag> shareHoldingFlagList = shareHoldingFlagRepository.findAll();
        assertThat(shareHoldingFlagList).hasSize(databaseSizeBeforeCreate);

        // Validate the ShareHoldingFlag in Elasticsearch
        verify(mockShareHoldingFlagSearchRepository, times(0)).save(shareHoldingFlag);
    }

    @Test
    @Transactional
    void checkShareholdingFlagTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = shareHoldingFlagRepository.findAll().size();
        // set the field null
        shareHoldingFlag.setShareholdingFlagTypeCode(null);

        // Create the ShareHoldingFlag, which fails.
        ShareHoldingFlagDTO shareHoldingFlagDTO = shareHoldingFlagMapper.toDto(shareHoldingFlag);

        restShareHoldingFlagMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareHoldingFlagDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShareHoldingFlag> shareHoldingFlagList = shareHoldingFlagRepository.findAll();
        assertThat(shareHoldingFlagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkShareholdingFlagTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = shareHoldingFlagRepository.findAll().size();
        // set the field null
        shareHoldingFlag.setShareholdingFlagType(null);

        // Create the ShareHoldingFlag, which fails.
        ShareHoldingFlagDTO shareHoldingFlagDTO = shareHoldingFlagMapper.toDto(shareHoldingFlag);

        restShareHoldingFlagMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareHoldingFlagDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShareHoldingFlag> shareHoldingFlagList = shareHoldingFlagRepository.findAll();
        assertThat(shareHoldingFlagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShareHoldingFlags() throws Exception {
        // Initialize the database
        shareHoldingFlagRepository.saveAndFlush(shareHoldingFlag);

        // Get all the shareHoldingFlagList
        restShareHoldingFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shareHoldingFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].shareholdingFlagTypeCode").value(hasItem(DEFAULT_SHAREHOLDING_FLAG_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].shareholdingFlagType").value(hasItem(DEFAULT_SHAREHOLDING_FLAG_TYPE)))
            .andExpect(jsonPath("$.[*].shareholdingTypeDescription").value(hasItem(DEFAULT_SHAREHOLDING_TYPE_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getShareHoldingFlag() throws Exception {
        // Initialize the database
        shareHoldingFlagRepository.saveAndFlush(shareHoldingFlag);

        // Get the shareHoldingFlag
        restShareHoldingFlagMockMvc
            .perform(get(ENTITY_API_URL_ID, shareHoldingFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shareHoldingFlag.getId().intValue()))
            .andExpect(jsonPath("$.shareholdingFlagTypeCode").value(DEFAULT_SHAREHOLDING_FLAG_TYPE_CODE.toString()))
            .andExpect(jsonPath("$.shareholdingFlagType").value(DEFAULT_SHAREHOLDING_FLAG_TYPE))
            .andExpect(jsonPath("$.shareholdingTypeDescription").value(DEFAULT_SHAREHOLDING_TYPE_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getShareHoldingFlagsByIdFiltering() throws Exception {
        // Initialize the database
        shareHoldingFlagRepository.saveAndFlush(shareHoldingFlag);

        Long id = shareHoldingFlag.getId();

        defaultShareHoldingFlagShouldBeFound("id.equals=" + id);
        defaultShareHoldingFlagShouldNotBeFound("id.notEquals=" + id);

        defaultShareHoldingFlagShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultShareHoldingFlagShouldNotBeFound("id.greaterThan=" + id);

        defaultShareHoldingFlagShouldBeFound("id.lessThanOrEqual=" + id);
        defaultShareHoldingFlagShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShareHoldingFlagsByShareholdingFlagTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        shareHoldingFlagRepository.saveAndFlush(shareHoldingFlag);

        // Get all the shareHoldingFlagList where shareholdingFlagTypeCode equals to DEFAULT_SHAREHOLDING_FLAG_TYPE_CODE
        defaultShareHoldingFlagShouldBeFound("shareholdingFlagTypeCode.equals=" + DEFAULT_SHAREHOLDING_FLAG_TYPE_CODE);

        // Get all the shareHoldingFlagList where shareholdingFlagTypeCode equals to UPDATED_SHAREHOLDING_FLAG_TYPE_CODE
        defaultShareHoldingFlagShouldNotBeFound("shareholdingFlagTypeCode.equals=" + UPDATED_SHAREHOLDING_FLAG_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllShareHoldingFlagsByShareholdingFlagTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shareHoldingFlagRepository.saveAndFlush(shareHoldingFlag);

        // Get all the shareHoldingFlagList where shareholdingFlagTypeCode not equals to DEFAULT_SHAREHOLDING_FLAG_TYPE_CODE
        defaultShareHoldingFlagShouldNotBeFound("shareholdingFlagTypeCode.notEquals=" + DEFAULT_SHAREHOLDING_FLAG_TYPE_CODE);

        // Get all the shareHoldingFlagList where shareholdingFlagTypeCode not equals to UPDATED_SHAREHOLDING_FLAG_TYPE_CODE
        defaultShareHoldingFlagShouldBeFound("shareholdingFlagTypeCode.notEquals=" + UPDATED_SHAREHOLDING_FLAG_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllShareHoldingFlagsByShareholdingFlagTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        shareHoldingFlagRepository.saveAndFlush(shareHoldingFlag);

        // Get all the shareHoldingFlagList where shareholdingFlagTypeCode in DEFAULT_SHAREHOLDING_FLAG_TYPE_CODE or UPDATED_SHAREHOLDING_FLAG_TYPE_CODE
        defaultShareHoldingFlagShouldBeFound(
            "shareholdingFlagTypeCode.in=" + DEFAULT_SHAREHOLDING_FLAG_TYPE_CODE + "," + UPDATED_SHAREHOLDING_FLAG_TYPE_CODE
        );

        // Get all the shareHoldingFlagList where shareholdingFlagTypeCode equals to UPDATED_SHAREHOLDING_FLAG_TYPE_CODE
        defaultShareHoldingFlagShouldNotBeFound("shareholdingFlagTypeCode.in=" + UPDATED_SHAREHOLDING_FLAG_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllShareHoldingFlagsByShareholdingFlagTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        shareHoldingFlagRepository.saveAndFlush(shareHoldingFlag);

        // Get all the shareHoldingFlagList where shareholdingFlagTypeCode is not null
        defaultShareHoldingFlagShouldBeFound("shareholdingFlagTypeCode.specified=true");

        // Get all the shareHoldingFlagList where shareholdingFlagTypeCode is null
        defaultShareHoldingFlagShouldNotBeFound("shareholdingFlagTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllShareHoldingFlagsByShareholdingFlagTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        shareHoldingFlagRepository.saveAndFlush(shareHoldingFlag);

        // Get all the shareHoldingFlagList where shareholdingFlagType equals to DEFAULT_SHAREHOLDING_FLAG_TYPE
        defaultShareHoldingFlagShouldBeFound("shareholdingFlagType.equals=" + DEFAULT_SHAREHOLDING_FLAG_TYPE);

        // Get all the shareHoldingFlagList where shareholdingFlagType equals to UPDATED_SHAREHOLDING_FLAG_TYPE
        defaultShareHoldingFlagShouldNotBeFound("shareholdingFlagType.equals=" + UPDATED_SHAREHOLDING_FLAG_TYPE);
    }

    @Test
    @Transactional
    void getAllShareHoldingFlagsByShareholdingFlagTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shareHoldingFlagRepository.saveAndFlush(shareHoldingFlag);

        // Get all the shareHoldingFlagList where shareholdingFlagType not equals to DEFAULT_SHAREHOLDING_FLAG_TYPE
        defaultShareHoldingFlagShouldNotBeFound("shareholdingFlagType.notEquals=" + DEFAULT_SHAREHOLDING_FLAG_TYPE);

        // Get all the shareHoldingFlagList where shareholdingFlagType not equals to UPDATED_SHAREHOLDING_FLAG_TYPE
        defaultShareHoldingFlagShouldBeFound("shareholdingFlagType.notEquals=" + UPDATED_SHAREHOLDING_FLAG_TYPE);
    }

    @Test
    @Transactional
    void getAllShareHoldingFlagsByShareholdingFlagTypeIsInShouldWork() throws Exception {
        // Initialize the database
        shareHoldingFlagRepository.saveAndFlush(shareHoldingFlag);

        // Get all the shareHoldingFlagList where shareholdingFlagType in DEFAULT_SHAREHOLDING_FLAG_TYPE or UPDATED_SHAREHOLDING_FLAG_TYPE
        defaultShareHoldingFlagShouldBeFound(
            "shareholdingFlagType.in=" + DEFAULT_SHAREHOLDING_FLAG_TYPE + "," + UPDATED_SHAREHOLDING_FLAG_TYPE
        );

        // Get all the shareHoldingFlagList where shareholdingFlagType equals to UPDATED_SHAREHOLDING_FLAG_TYPE
        defaultShareHoldingFlagShouldNotBeFound("shareholdingFlagType.in=" + UPDATED_SHAREHOLDING_FLAG_TYPE);
    }

    @Test
    @Transactional
    void getAllShareHoldingFlagsByShareholdingFlagTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        shareHoldingFlagRepository.saveAndFlush(shareHoldingFlag);

        // Get all the shareHoldingFlagList where shareholdingFlagType is not null
        defaultShareHoldingFlagShouldBeFound("shareholdingFlagType.specified=true");

        // Get all the shareHoldingFlagList where shareholdingFlagType is null
        defaultShareHoldingFlagShouldNotBeFound("shareholdingFlagType.specified=false");
    }

    @Test
    @Transactional
    void getAllShareHoldingFlagsByShareholdingFlagTypeContainsSomething() throws Exception {
        // Initialize the database
        shareHoldingFlagRepository.saveAndFlush(shareHoldingFlag);

        // Get all the shareHoldingFlagList where shareholdingFlagType contains DEFAULT_SHAREHOLDING_FLAG_TYPE
        defaultShareHoldingFlagShouldBeFound("shareholdingFlagType.contains=" + DEFAULT_SHAREHOLDING_FLAG_TYPE);

        // Get all the shareHoldingFlagList where shareholdingFlagType contains UPDATED_SHAREHOLDING_FLAG_TYPE
        defaultShareHoldingFlagShouldNotBeFound("shareholdingFlagType.contains=" + UPDATED_SHAREHOLDING_FLAG_TYPE);
    }

    @Test
    @Transactional
    void getAllShareHoldingFlagsByShareholdingFlagTypeNotContainsSomething() throws Exception {
        // Initialize the database
        shareHoldingFlagRepository.saveAndFlush(shareHoldingFlag);

        // Get all the shareHoldingFlagList where shareholdingFlagType does not contain DEFAULT_SHAREHOLDING_FLAG_TYPE
        defaultShareHoldingFlagShouldNotBeFound("shareholdingFlagType.doesNotContain=" + DEFAULT_SHAREHOLDING_FLAG_TYPE);

        // Get all the shareHoldingFlagList where shareholdingFlagType does not contain UPDATED_SHAREHOLDING_FLAG_TYPE
        defaultShareHoldingFlagShouldBeFound("shareholdingFlagType.doesNotContain=" + UPDATED_SHAREHOLDING_FLAG_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShareHoldingFlagShouldBeFound(String filter) throws Exception {
        restShareHoldingFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shareHoldingFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].shareholdingFlagTypeCode").value(hasItem(DEFAULT_SHAREHOLDING_FLAG_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].shareholdingFlagType").value(hasItem(DEFAULT_SHAREHOLDING_FLAG_TYPE)))
            .andExpect(jsonPath("$.[*].shareholdingTypeDescription").value(hasItem(DEFAULT_SHAREHOLDING_TYPE_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restShareHoldingFlagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShareHoldingFlagShouldNotBeFound(String filter) throws Exception {
        restShareHoldingFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShareHoldingFlagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShareHoldingFlag() throws Exception {
        // Get the shareHoldingFlag
        restShareHoldingFlagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewShareHoldingFlag() throws Exception {
        // Initialize the database
        shareHoldingFlagRepository.saveAndFlush(shareHoldingFlag);

        int databaseSizeBeforeUpdate = shareHoldingFlagRepository.findAll().size();

        // Update the shareHoldingFlag
        ShareHoldingFlag updatedShareHoldingFlag = shareHoldingFlagRepository.findById(shareHoldingFlag.getId()).get();
        // Disconnect from session so that the updates on updatedShareHoldingFlag are not directly saved in db
        em.detach(updatedShareHoldingFlag);
        updatedShareHoldingFlag
            .shareholdingFlagTypeCode(UPDATED_SHAREHOLDING_FLAG_TYPE_CODE)
            .shareholdingFlagType(UPDATED_SHAREHOLDING_FLAG_TYPE)
            .shareholdingTypeDescription(UPDATED_SHAREHOLDING_TYPE_DESCRIPTION);
        ShareHoldingFlagDTO shareHoldingFlagDTO = shareHoldingFlagMapper.toDto(updatedShareHoldingFlag);

        restShareHoldingFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shareHoldingFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shareHoldingFlagDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShareHoldingFlag in the database
        List<ShareHoldingFlag> shareHoldingFlagList = shareHoldingFlagRepository.findAll();
        assertThat(shareHoldingFlagList).hasSize(databaseSizeBeforeUpdate);
        ShareHoldingFlag testShareHoldingFlag = shareHoldingFlagList.get(shareHoldingFlagList.size() - 1);
        assertThat(testShareHoldingFlag.getShareholdingFlagTypeCode()).isEqualTo(UPDATED_SHAREHOLDING_FLAG_TYPE_CODE);
        assertThat(testShareHoldingFlag.getShareholdingFlagType()).isEqualTo(UPDATED_SHAREHOLDING_FLAG_TYPE);
        assertThat(testShareHoldingFlag.getShareholdingTypeDescription()).isEqualTo(UPDATED_SHAREHOLDING_TYPE_DESCRIPTION);

        // Validate the ShareHoldingFlag in Elasticsearch
        verify(mockShareHoldingFlagSearchRepository).save(testShareHoldingFlag);
    }

    @Test
    @Transactional
    void putNonExistingShareHoldingFlag() throws Exception {
        int databaseSizeBeforeUpdate = shareHoldingFlagRepository.findAll().size();
        shareHoldingFlag.setId(count.incrementAndGet());

        // Create the ShareHoldingFlag
        ShareHoldingFlagDTO shareHoldingFlagDTO = shareHoldingFlagMapper.toDto(shareHoldingFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShareHoldingFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shareHoldingFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shareHoldingFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareHoldingFlag in the database
        List<ShareHoldingFlag> shareHoldingFlagList = shareHoldingFlagRepository.findAll();
        assertThat(shareHoldingFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShareHoldingFlag in Elasticsearch
        verify(mockShareHoldingFlagSearchRepository, times(0)).save(shareHoldingFlag);
    }

    @Test
    @Transactional
    void putWithIdMismatchShareHoldingFlag() throws Exception {
        int databaseSizeBeforeUpdate = shareHoldingFlagRepository.findAll().size();
        shareHoldingFlag.setId(count.incrementAndGet());

        // Create the ShareHoldingFlag
        ShareHoldingFlagDTO shareHoldingFlagDTO = shareHoldingFlagMapper.toDto(shareHoldingFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareHoldingFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shareHoldingFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareHoldingFlag in the database
        List<ShareHoldingFlag> shareHoldingFlagList = shareHoldingFlagRepository.findAll();
        assertThat(shareHoldingFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShareHoldingFlag in Elasticsearch
        verify(mockShareHoldingFlagSearchRepository, times(0)).save(shareHoldingFlag);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShareHoldingFlag() throws Exception {
        int databaseSizeBeforeUpdate = shareHoldingFlagRepository.findAll().size();
        shareHoldingFlag.setId(count.incrementAndGet());

        // Create the ShareHoldingFlag
        ShareHoldingFlagDTO shareHoldingFlagDTO = shareHoldingFlagMapper.toDto(shareHoldingFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareHoldingFlagMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shareHoldingFlagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShareHoldingFlag in the database
        List<ShareHoldingFlag> shareHoldingFlagList = shareHoldingFlagRepository.findAll();
        assertThat(shareHoldingFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShareHoldingFlag in Elasticsearch
        verify(mockShareHoldingFlagSearchRepository, times(0)).save(shareHoldingFlag);
    }

    @Test
    @Transactional
    void partialUpdateShareHoldingFlagWithPatch() throws Exception {
        // Initialize the database
        shareHoldingFlagRepository.saveAndFlush(shareHoldingFlag);

        int databaseSizeBeforeUpdate = shareHoldingFlagRepository.findAll().size();

        // Update the shareHoldingFlag using partial update
        ShareHoldingFlag partialUpdatedShareHoldingFlag = new ShareHoldingFlag();
        partialUpdatedShareHoldingFlag.setId(shareHoldingFlag.getId());

        partialUpdatedShareHoldingFlag.shareholdingFlagTypeCode(UPDATED_SHAREHOLDING_FLAG_TYPE_CODE);

        restShareHoldingFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShareHoldingFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShareHoldingFlag))
            )
            .andExpect(status().isOk());

        // Validate the ShareHoldingFlag in the database
        List<ShareHoldingFlag> shareHoldingFlagList = shareHoldingFlagRepository.findAll();
        assertThat(shareHoldingFlagList).hasSize(databaseSizeBeforeUpdate);
        ShareHoldingFlag testShareHoldingFlag = shareHoldingFlagList.get(shareHoldingFlagList.size() - 1);
        assertThat(testShareHoldingFlag.getShareholdingFlagTypeCode()).isEqualTo(UPDATED_SHAREHOLDING_FLAG_TYPE_CODE);
        assertThat(testShareHoldingFlag.getShareholdingFlagType()).isEqualTo(DEFAULT_SHAREHOLDING_FLAG_TYPE);
        assertThat(testShareHoldingFlag.getShareholdingTypeDescription()).isEqualTo(DEFAULT_SHAREHOLDING_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateShareHoldingFlagWithPatch() throws Exception {
        // Initialize the database
        shareHoldingFlagRepository.saveAndFlush(shareHoldingFlag);

        int databaseSizeBeforeUpdate = shareHoldingFlagRepository.findAll().size();

        // Update the shareHoldingFlag using partial update
        ShareHoldingFlag partialUpdatedShareHoldingFlag = new ShareHoldingFlag();
        partialUpdatedShareHoldingFlag.setId(shareHoldingFlag.getId());

        partialUpdatedShareHoldingFlag
            .shareholdingFlagTypeCode(UPDATED_SHAREHOLDING_FLAG_TYPE_CODE)
            .shareholdingFlagType(UPDATED_SHAREHOLDING_FLAG_TYPE)
            .shareholdingTypeDescription(UPDATED_SHAREHOLDING_TYPE_DESCRIPTION);

        restShareHoldingFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShareHoldingFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShareHoldingFlag))
            )
            .andExpect(status().isOk());

        // Validate the ShareHoldingFlag in the database
        List<ShareHoldingFlag> shareHoldingFlagList = shareHoldingFlagRepository.findAll();
        assertThat(shareHoldingFlagList).hasSize(databaseSizeBeforeUpdate);
        ShareHoldingFlag testShareHoldingFlag = shareHoldingFlagList.get(shareHoldingFlagList.size() - 1);
        assertThat(testShareHoldingFlag.getShareholdingFlagTypeCode()).isEqualTo(UPDATED_SHAREHOLDING_FLAG_TYPE_CODE);
        assertThat(testShareHoldingFlag.getShareholdingFlagType()).isEqualTo(UPDATED_SHAREHOLDING_FLAG_TYPE);
        assertThat(testShareHoldingFlag.getShareholdingTypeDescription()).isEqualTo(UPDATED_SHAREHOLDING_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingShareHoldingFlag() throws Exception {
        int databaseSizeBeforeUpdate = shareHoldingFlagRepository.findAll().size();
        shareHoldingFlag.setId(count.incrementAndGet());

        // Create the ShareHoldingFlag
        ShareHoldingFlagDTO shareHoldingFlagDTO = shareHoldingFlagMapper.toDto(shareHoldingFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShareHoldingFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shareHoldingFlagDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shareHoldingFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareHoldingFlag in the database
        List<ShareHoldingFlag> shareHoldingFlagList = shareHoldingFlagRepository.findAll();
        assertThat(shareHoldingFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShareHoldingFlag in Elasticsearch
        verify(mockShareHoldingFlagSearchRepository, times(0)).save(shareHoldingFlag);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShareHoldingFlag() throws Exception {
        int databaseSizeBeforeUpdate = shareHoldingFlagRepository.findAll().size();
        shareHoldingFlag.setId(count.incrementAndGet());

        // Create the ShareHoldingFlag
        ShareHoldingFlagDTO shareHoldingFlagDTO = shareHoldingFlagMapper.toDto(shareHoldingFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareHoldingFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shareHoldingFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShareHoldingFlag in the database
        List<ShareHoldingFlag> shareHoldingFlagList = shareHoldingFlagRepository.findAll();
        assertThat(shareHoldingFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShareHoldingFlag in Elasticsearch
        verify(mockShareHoldingFlagSearchRepository, times(0)).save(shareHoldingFlag);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShareHoldingFlag() throws Exception {
        int databaseSizeBeforeUpdate = shareHoldingFlagRepository.findAll().size();
        shareHoldingFlag.setId(count.incrementAndGet());

        // Create the ShareHoldingFlag
        ShareHoldingFlagDTO shareHoldingFlagDTO = shareHoldingFlagMapper.toDto(shareHoldingFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShareHoldingFlagMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shareHoldingFlagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShareHoldingFlag in the database
        List<ShareHoldingFlag> shareHoldingFlagList = shareHoldingFlagRepository.findAll();
        assertThat(shareHoldingFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShareHoldingFlag in Elasticsearch
        verify(mockShareHoldingFlagSearchRepository, times(0)).save(shareHoldingFlag);
    }

    @Test
    @Transactional
    void deleteShareHoldingFlag() throws Exception {
        // Initialize the database
        shareHoldingFlagRepository.saveAndFlush(shareHoldingFlag);

        int databaseSizeBeforeDelete = shareHoldingFlagRepository.findAll().size();

        // Delete the shareHoldingFlag
        restShareHoldingFlagMockMvc
            .perform(delete(ENTITY_API_URL_ID, shareHoldingFlag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShareHoldingFlag> shareHoldingFlagList = shareHoldingFlagRepository.findAll();
        assertThat(shareHoldingFlagList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ShareHoldingFlag in Elasticsearch
        verify(mockShareHoldingFlagSearchRepository, times(1)).deleteById(shareHoldingFlag.getId());
    }

    @Test
    @Transactional
    void searchShareHoldingFlag() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        shareHoldingFlagRepository.saveAndFlush(shareHoldingFlag);
        when(mockShareHoldingFlagSearchRepository.search("id:" + shareHoldingFlag.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(shareHoldingFlag), PageRequest.of(0, 1), 1));

        // Search the shareHoldingFlag
        restShareHoldingFlagMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + shareHoldingFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shareHoldingFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].shareholdingFlagTypeCode").value(hasItem(DEFAULT_SHAREHOLDING_FLAG_TYPE_CODE.toString())))
            .andExpect(jsonPath("$.[*].shareholdingFlagType").value(hasItem(DEFAULT_SHAREHOLDING_FLAG_TYPE)))
            .andExpect(jsonPath("$.[*].shareholdingTypeDescription").value(hasItem(DEFAULT_SHAREHOLDING_TYPE_DESCRIPTION.toString())));
    }
}
