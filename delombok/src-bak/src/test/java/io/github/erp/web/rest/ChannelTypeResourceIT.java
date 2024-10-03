package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.ChannelType;
import io.github.erp.repository.ChannelTypeRepository;
import io.github.erp.repository.search.ChannelTypeSearchRepository;
import io.github.erp.service.criteria.ChannelTypeCriteria;
import io.github.erp.service.dto.ChannelTypeDTO;
import io.github.erp.service.mapper.ChannelTypeMapper;
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
 * Integration tests for the {@link ChannelTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ChannelTypeResourceIT {

    private static final String DEFAULT_CHANNELS_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CHANNELS_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CHANNEL_TYPES = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL_TYPES = "BBBBBBBBBB";

    private static final String DEFAULT_CHANNEL_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/channel-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/channel-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChannelTypeRepository channelTypeRepository;

    @Autowired
    private ChannelTypeMapper channelTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ChannelTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ChannelTypeSearchRepository mockChannelTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChannelTypeMockMvc;

    private ChannelType channelType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChannelType createEntity(EntityManager em) {
        ChannelType channelType = new ChannelType()
            .channelsTypeCode(DEFAULT_CHANNELS_TYPE_CODE)
            .channelTypes(DEFAULT_CHANNEL_TYPES)
            .channelTypeDetails(DEFAULT_CHANNEL_TYPE_DETAILS);
        return channelType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChannelType createUpdatedEntity(EntityManager em) {
        ChannelType channelType = new ChannelType()
            .channelsTypeCode(UPDATED_CHANNELS_TYPE_CODE)
            .channelTypes(UPDATED_CHANNEL_TYPES)
            .channelTypeDetails(UPDATED_CHANNEL_TYPE_DETAILS);
        return channelType;
    }

    @BeforeEach
    public void initTest() {
        channelType = createEntity(em);
    }

    @Test
    @Transactional
    void createChannelType() throws Exception {
        int databaseSizeBeforeCreate = channelTypeRepository.findAll().size();
        // Create the ChannelType
        ChannelTypeDTO channelTypeDTO = channelTypeMapper.toDto(channelType);
        restChannelTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(channelTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ChannelType in the database
        List<ChannelType> channelTypeList = channelTypeRepository.findAll();
        assertThat(channelTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ChannelType testChannelType = channelTypeList.get(channelTypeList.size() - 1);
        assertThat(testChannelType.getChannelsTypeCode()).isEqualTo(DEFAULT_CHANNELS_TYPE_CODE);
        assertThat(testChannelType.getChannelTypes()).isEqualTo(DEFAULT_CHANNEL_TYPES);
        assertThat(testChannelType.getChannelTypeDetails()).isEqualTo(DEFAULT_CHANNEL_TYPE_DETAILS);

        // Validate the ChannelType in Elasticsearch
        verify(mockChannelTypeSearchRepository, times(1)).save(testChannelType);
    }

    @Test
    @Transactional
    void createChannelTypeWithExistingId() throws Exception {
        // Create the ChannelType with an existing ID
        channelType.setId(1L);
        ChannelTypeDTO channelTypeDTO = channelTypeMapper.toDto(channelType);

        int databaseSizeBeforeCreate = channelTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChannelTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(channelTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChannelType in the database
        List<ChannelType> channelTypeList = channelTypeRepository.findAll();
        assertThat(channelTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the ChannelType in Elasticsearch
        verify(mockChannelTypeSearchRepository, times(0)).save(channelType);
    }

    @Test
    @Transactional
    void checkChannelsTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = channelTypeRepository.findAll().size();
        // set the field null
        channelType.setChannelsTypeCode(null);

        // Create the ChannelType, which fails.
        ChannelTypeDTO channelTypeDTO = channelTypeMapper.toDto(channelType);

        restChannelTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(channelTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ChannelType> channelTypeList = channelTypeRepository.findAll();
        assertThat(channelTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChannelTypesIsRequired() throws Exception {
        int databaseSizeBeforeTest = channelTypeRepository.findAll().size();
        // set the field null
        channelType.setChannelTypes(null);

        // Create the ChannelType, which fails.
        ChannelTypeDTO channelTypeDTO = channelTypeMapper.toDto(channelType);

        restChannelTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(channelTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ChannelType> channelTypeList = channelTypeRepository.findAll();
        assertThat(channelTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChannelTypes() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        // Get all the channelTypeList
        restChannelTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(channelType.getId().intValue())))
            .andExpect(jsonPath("$.[*].channelsTypeCode").value(hasItem(DEFAULT_CHANNELS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].channelTypes").value(hasItem(DEFAULT_CHANNEL_TYPES)))
            .andExpect(jsonPath("$.[*].channelTypeDetails").value(hasItem(DEFAULT_CHANNEL_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getChannelType() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        // Get the channelType
        restChannelTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, channelType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(channelType.getId().intValue()))
            .andExpect(jsonPath("$.channelsTypeCode").value(DEFAULT_CHANNELS_TYPE_CODE))
            .andExpect(jsonPath("$.channelTypes").value(DEFAULT_CHANNEL_TYPES))
            .andExpect(jsonPath("$.channelTypeDetails").value(DEFAULT_CHANNEL_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getChannelTypesByIdFiltering() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        Long id = channelType.getId();

        defaultChannelTypeShouldBeFound("id.equals=" + id);
        defaultChannelTypeShouldNotBeFound("id.notEquals=" + id);

        defaultChannelTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultChannelTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultChannelTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultChannelTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllChannelTypesByChannelsTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        // Get all the channelTypeList where channelsTypeCode equals to DEFAULT_CHANNELS_TYPE_CODE
        defaultChannelTypeShouldBeFound("channelsTypeCode.equals=" + DEFAULT_CHANNELS_TYPE_CODE);

        // Get all the channelTypeList where channelsTypeCode equals to UPDATED_CHANNELS_TYPE_CODE
        defaultChannelTypeShouldNotBeFound("channelsTypeCode.equals=" + UPDATED_CHANNELS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllChannelTypesByChannelsTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        // Get all the channelTypeList where channelsTypeCode not equals to DEFAULT_CHANNELS_TYPE_CODE
        defaultChannelTypeShouldNotBeFound("channelsTypeCode.notEquals=" + DEFAULT_CHANNELS_TYPE_CODE);

        // Get all the channelTypeList where channelsTypeCode not equals to UPDATED_CHANNELS_TYPE_CODE
        defaultChannelTypeShouldBeFound("channelsTypeCode.notEquals=" + UPDATED_CHANNELS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllChannelTypesByChannelsTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        // Get all the channelTypeList where channelsTypeCode in DEFAULT_CHANNELS_TYPE_CODE or UPDATED_CHANNELS_TYPE_CODE
        defaultChannelTypeShouldBeFound("channelsTypeCode.in=" + DEFAULT_CHANNELS_TYPE_CODE + "," + UPDATED_CHANNELS_TYPE_CODE);

        // Get all the channelTypeList where channelsTypeCode equals to UPDATED_CHANNELS_TYPE_CODE
        defaultChannelTypeShouldNotBeFound("channelsTypeCode.in=" + UPDATED_CHANNELS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllChannelTypesByChannelsTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        // Get all the channelTypeList where channelsTypeCode is not null
        defaultChannelTypeShouldBeFound("channelsTypeCode.specified=true");

        // Get all the channelTypeList where channelsTypeCode is null
        defaultChannelTypeShouldNotBeFound("channelsTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllChannelTypesByChannelsTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        // Get all the channelTypeList where channelsTypeCode contains DEFAULT_CHANNELS_TYPE_CODE
        defaultChannelTypeShouldBeFound("channelsTypeCode.contains=" + DEFAULT_CHANNELS_TYPE_CODE);

        // Get all the channelTypeList where channelsTypeCode contains UPDATED_CHANNELS_TYPE_CODE
        defaultChannelTypeShouldNotBeFound("channelsTypeCode.contains=" + UPDATED_CHANNELS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllChannelTypesByChannelsTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        // Get all the channelTypeList where channelsTypeCode does not contain DEFAULT_CHANNELS_TYPE_CODE
        defaultChannelTypeShouldNotBeFound("channelsTypeCode.doesNotContain=" + DEFAULT_CHANNELS_TYPE_CODE);

        // Get all the channelTypeList where channelsTypeCode does not contain UPDATED_CHANNELS_TYPE_CODE
        defaultChannelTypeShouldBeFound("channelsTypeCode.doesNotContain=" + UPDATED_CHANNELS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllChannelTypesByChannelTypesIsEqualToSomething() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        // Get all the channelTypeList where channelTypes equals to DEFAULT_CHANNEL_TYPES
        defaultChannelTypeShouldBeFound("channelTypes.equals=" + DEFAULT_CHANNEL_TYPES);

        // Get all the channelTypeList where channelTypes equals to UPDATED_CHANNEL_TYPES
        defaultChannelTypeShouldNotBeFound("channelTypes.equals=" + UPDATED_CHANNEL_TYPES);
    }

    @Test
    @Transactional
    void getAllChannelTypesByChannelTypesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        // Get all the channelTypeList where channelTypes not equals to DEFAULT_CHANNEL_TYPES
        defaultChannelTypeShouldNotBeFound("channelTypes.notEquals=" + DEFAULT_CHANNEL_TYPES);

        // Get all the channelTypeList where channelTypes not equals to UPDATED_CHANNEL_TYPES
        defaultChannelTypeShouldBeFound("channelTypes.notEquals=" + UPDATED_CHANNEL_TYPES);
    }

    @Test
    @Transactional
    void getAllChannelTypesByChannelTypesIsInShouldWork() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        // Get all the channelTypeList where channelTypes in DEFAULT_CHANNEL_TYPES or UPDATED_CHANNEL_TYPES
        defaultChannelTypeShouldBeFound("channelTypes.in=" + DEFAULT_CHANNEL_TYPES + "," + UPDATED_CHANNEL_TYPES);

        // Get all the channelTypeList where channelTypes equals to UPDATED_CHANNEL_TYPES
        defaultChannelTypeShouldNotBeFound("channelTypes.in=" + UPDATED_CHANNEL_TYPES);
    }

    @Test
    @Transactional
    void getAllChannelTypesByChannelTypesIsNullOrNotNull() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        // Get all the channelTypeList where channelTypes is not null
        defaultChannelTypeShouldBeFound("channelTypes.specified=true");

        // Get all the channelTypeList where channelTypes is null
        defaultChannelTypeShouldNotBeFound("channelTypes.specified=false");
    }

    @Test
    @Transactional
    void getAllChannelTypesByChannelTypesContainsSomething() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        // Get all the channelTypeList where channelTypes contains DEFAULT_CHANNEL_TYPES
        defaultChannelTypeShouldBeFound("channelTypes.contains=" + DEFAULT_CHANNEL_TYPES);

        // Get all the channelTypeList where channelTypes contains UPDATED_CHANNEL_TYPES
        defaultChannelTypeShouldNotBeFound("channelTypes.contains=" + UPDATED_CHANNEL_TYPES);
    }

    @Test
    @Transactional
    void getAllChannelTypesByChannelTypesNotContainsSomething() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        // Get all the channelTypeList where channelTypes does not contain DEFAULT_CHANNEL_TYPES
        defaultChannelTypeShouldNotBeFound("channelTypes.doesNotContain=" + DEFAULT_CHANNEL_TYPES);

        // Get all the channelTypeList where channelTypes does not contain UPDATED_CHANNEL_TYPES
        defaultChannelTypeShouldBeFound("channelTypes.doesNotContain=" + UPDATED_CHANNEL_TYPES);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChannelTypeShouldBeFound(String filter) throws Exception {
        restChannelTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(channelType.getId().intValue())))
            .andExpect(jsonPath("$.[*].channelsTypeCode").value(hasItem(DEFAULT_CHANNELS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].channelTypes").value(hasItem(DEFAULT_CHANNEL_TYPES)))
            .andExpect(jsonPath("$.[*].channelTypeDetails").value(hasItem(DEFAULT_CHANNEL_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restChannelTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChannelTypeShouldNotBeFound(String filter) throws Exception {
        restChannelTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChannelTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingChannelType() throws Exception {
        // Get the channelType
        restChannelTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChannelType() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        int databaseSizeBeforeUpdate = channelTypeRepository.findAll().size();

        // Update the channelType
        ChannelType updatedChannelType = channelTypeRepository.findById(channelType.getId()).get();
        // Disconnect from session so that the updates on updatedChannelType are not directly saved in db
        em.detach(updatedChannelType);
        updatedChannelType
            .channelsTypeCode(UPDATED_CHANNELS_TYPE_CODE)
            .channelTypes(UPDATED_CHANNEL_TYPES)
            .channelTypeDetails(UPDATED_CHANNEL_TYPE_DETAILS);
        ChannelTypeDTO channelTypeDTO = channelTypeMapper.toDto(updatedChannelType);

        restChannelTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, channelTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(channelTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ChannelType in the database
        List<ChannelType> channelTypeList = channelTypeRepository.findAll();
        assertThat(channelTypeList).hasSize(databaseSizeBeforeUpdate);
        ChannelType testChannelType = channelTypeList.get(channelTypeList.size() - 1);
        assertThat(testChannelType.getChannelsTypeCode()).isEqualTo(UPDATED_CHANNELS_TYPE_CODE);
        assertThat(testChannelType.getChannelTypes()).isEqualTo(UPDATED_CHANNEL_TYPES);
        assertThat(testChannelType.getChannelTypeDetails()).isEqualTo(UPDATED_CHANNEL_TYPE_DETAILS);

        // Validate the ChannelType in Elasticsearch
        verify(mockChannelTypeSearchRepository).save(testChannelType);
    }

    @Test
    @Transactional
    void putNonExistingChannelType() throws Exception {
        int databaseSizeBeforeUpdate = channelTypeRepository.findAll().size();
        channelType.setId(count.incrementAndGet());

        // Create the ChannelType
        ChannelTypeDTO channelTypeDTO = channelTypeMapper.toDto(channelType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChannelTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, channelTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(channelTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChannelType in the database
        List<ChannelType> channelTypeList = channelTypeRepository.findAll();
        assertThat(channelTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ChannelType in Elasticsearch
        verify(mockChannelTypeSearchRepository, times(0)).save(channelType);
    }

    @Test
    @Transactional
    void putWithIdMismatchChannelType() throws Exception {
        int databaseSizeBeforeUpdate = channelTypeRepository.findAll().size();
        channelType.setId(count.incrementAndGet());

        // Create the ChannelType
        ChannelTypeDTO channelTypeDTO = channelTypeMapper.toDto(channelType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChannelTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(channelTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChannelType in the database
        List<ChannelType> channelTypeList = channelTypeRepository.findAll();
        assertThat(channelTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ChannelType in Elasticsearch
        verify(mockChannelTypeSearchRepository, times(0)).save(channelType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChannelType() throws Exception {
        int databaseSizeBeforeUpdate = channelTypeRepository.findAll().size();
        channelType.setId(count.incrementAndGet());

        // Create the ChannelType
        ChannelTypeDTO channelTypeDTO = channelTypeMapper.toDto(channelType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChannelTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(channelTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChannelType in the database
        List<ChannelType> channelTypeList = channelTypeRepository.findAll();
        assertThat(channelTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ChannelType in Elasticsearch
        verify(mockChannelTypeSearchRepository, times(0)).save(channelType);
    }

    @Test
    @Transactional
    void partialUpdateChannelTypeWithPatch() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        int databaseSizeBeforeUpdate = channelTypeRepository.findAll().size();

        // Update the channelType using partial update
        ChannelType partialUpdatedChannelType = new ChannelType();
        partialUpdatedChannelType.setId(channelType.getId());

        partialUpdatedChannelType.channelTypes(UPDATED_CHANNEL_TYPES);

        restChannelTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChannelType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChannelType))
            )
            .andExpect(status().isOk());

        // Validate the ChannelType in the database
        List<ChannelType> channelTypeList = channelTypeRepository.findAll();
        assertThat(channelTypeList).hasSize(databaseSizeBeforeUpdate);
        ChannelType testChannelType = channelTypeList.get(channelTypeList.size() - 1);
        assertThat(testChannelType.getChannelsTypeCode()).isEqualTo(DEFAULT_CHANNELS_TYPE_CODE);
        assertThat(testChannelType.getChannelTypes()).isEqualTo(UPDATED_CHANNEL_TYPES);
        assertThat(testChannelType.getChannelTypeDetails()).isEqualTo(DEFAULT_CHANNEL_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateChannelTypeWithPatch() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        int databaseSizeBeforeUpdate = channelTypeRepository.findAll().size();

        // Update the channelType using partial update
        ChannelType partialUpdatedChannelType = new ChannelType();
        partialUpdatedChannelType.setId(channelType.getId());

        partialUpdatedChannelType
            .channelsTypeCode(UPDATED_CHANNELS_TYPE_CODE)
            .channelTypes(UPDATED_CHANNEL_TYPES)
            .channelTypeDetails(UPDATED_CHANNEL_TYPE_DETAILS);

        restChannelTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChannelType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChannelType))
            )
            .andExpect(status().isOk());

        // Validate the ChannelType in the database
        List<ChannelType> channelTypeList = channelTypeRepository.findAll();
        assertThat(channelTypeList).hasSize(databaseSizeBeforeUpdate);
        ChannelType testChannelType = channelTypeList.get(channelTypeList.size() - 1);
        assertThat(testChannelType.getChannelsTypeCode()).isEqualTo(UPDATED_CHANNELS_TYPE_CODE);
        assertThat(testChannelType.getChannelTypes()).isEqualTo(UPDATED_CHANNEL_TYPES);
        assertThat(testChannelType.getChannelTypeDetails()).isEqualTo(UPDATED_CHANNEL_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingChannelType() throws Exception {
        int databaseSizeBeforeUpdate = channelTypeRepository.findAll().size();
        channelType.setId(count.incrementAndGet());

        // Create the ChannelType
        ChannelTypeDTO channelTypeDTO = channelTypeMapper.toDto(channelType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChannelTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, channelTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(channelTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChannelType in the database
        List<ChannelType> channelTypeList = channelTypeRepository.findAll();
        assertThat(channelTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ChannelType in Elasticsearch
        verify(mockChannelTypeSearchRepository, times(0)).save(channelType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChannelType() throws Exception {
        int databaseSizeBeforeUpdate = channelTypeRepository.findAll().size();
        channelType.setId(count.incrementAndGet());

        // Create the ChannelType
        ChannelTypeDTO channelTypeDTO = channelTypeMapper.toDto(channelType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChannelTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(channelTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChannelType in the database
        List<ChannelType> channelTypeList = channelTypeRepository.findAll();
        assertThat(channelTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ChannelType in Elasticsearch
        verify(mockChannelTypeSearchRepository, times(0)).save(channelType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChannelType() throws Exception {
        int databaseSizeBeforeUpdate = channelTypeRepository.findAll().size();
        channelType.setId(count.incrementAndGet());

        // Create the ChannelType
        ChannelTypeDTO channelTypeDTO = channelTypeMapper.toDto(channelType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChannelTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(channelTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChannelType in the database
        List<ChannelType> channelTypeList = channelTypeRepository.findAll();
        assertThat(channelTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ChannelType in Elasticsearch
        verify(mockChannelTypeSearchRepository, times(0)).save(channelType);
    }

    @Test
    @Transactional
    void deleteChannelType() throws Exception {
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);

        int databaseSizeBeforeDelete = channelTypeRepository.findAll().size();

        // Delete the channelType
        restChannelTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, channelType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChannelType> channelTypeList = channelTypeRepository.findAll();
        assertThat(channelTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ChannelType in Elasticsearch
        verify(mockChannelTypeSearchRepository, times(1)).deleteById(channelType.getId());
    }

    @Test
    @Transactional
    void searchChannelType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        channelTypeRepository.saveAndFlush(channelType);
        when(mockChannelTypeSearchRepository.search("id:" + channelType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(channelType), PageRequest.of(0, 1), 1));

        // Search the channelType
        restChannelTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + channelType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(channelType.getId().intValue())))
            .andExpect(jsonPath("$.[*].channelsTypeCode").value(hasItem(DEFAULT_CHANNELS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].channelTypes").value(hasItem(DEFAULT_CHANNEL_TYPES)))
            .andExpect(jsonPath("$.[*].channelTypeDetails").value(hasItem(DEFAULT_CHANNEL_TYPE_DETAILS.toString())));
    }
}
