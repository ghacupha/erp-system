package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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
import io.github.erp.domain.MoratoriumItem;
import io.github.erp.repository.MoratoriumItemRepository;
import io.github.erp.repository.search.MoratoriumItemSearchRepository;
import io.github.erp.service.criteria.MoratoriumItemCriteria;
import io.github.erp.service.dto.MoratoriumItemDTO;
import io.github.erp.service.mapper.MoratoriumItemMapper;
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
 * Integration tests for the {@link MoratoriumItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MoratoriumItemResourceIT {

    private static final String DEFAULT_MORATORIUM_ITEM_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MORATORIUM_ITEM_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MORATORIUM_ITEM_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MORATORIUM_ITEM_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MORATORIUM_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_MORATORIUM_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/moratorium-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/moratorium-items";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MoratoriumItemRepository moratoriumItemRepository;

    @Autowired
    private MoratoriumItemMapper moratoriumItemMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.MoratoriumItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private MoratoriumItemSearchRepository mockMoratoriumItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMoratoriumItemMockMvc;

    private MoratoriumItem moratoriumItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoratoriumItem createEntity(EntityManager em) {
        MoratoriumItem moratoriumItem = new MoratoriumItem()
            .moratoriumItemTypeCode(DEFAULT_MORATORIUM_ITEM_TYPE_CODE)
            .moratoriumItemType(DEFAULT_MORATORIUM_ITEM_TYPE)
            .moratoriumTypeDetails(DEFAULT_MORATORIUM_TYPE_DETAILS);
        return moratoriumItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoratoriumItem createUpdatedEntity(EntityManager em) {
        MoratoriumItem moratoriumItem = new MoratoriumItem()
            .moratoriumItemTypeCode(UPDATED_MORATORIUM_ITEM_TYPE_CODE)
            .moratoriumItemType(UPDATED_MORATORIUM_ITEM_TYPE)
            .moratoriumTypeDetails(UPDATED_MORATORIUM_TYPE_DETAILS);
        return moratoriumItem;
    }

    @BeforeEach
    public void initTest() {
        moratoriumItem = createEntity(em);
    }

    @Test
    @Transactional
    void createMoratoriumItem() throws Exception {
        int databaseSizeBeforeCreate = moratoriumItemRepository.findAll().size();
        // Create the MoratoriumItem
        MoratoriumItemDTO moratoriumItemDTO = moratoriumItemMapper.toDto(moratoriumItem);
        restMoratoriumItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moratoriumItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MoratoriumItem in the database
        List<MoratoriumItem> moratoriumItemList = moratoriumItemRepository.findAll();
        assertThat(moratoriumItemList).hasSize(databaseSizeBeforeCreate + 1);
        MoratoriumItem testMoratoriumItem = moratoriumItemList.get(moratoriumItemList.size() - 1);
        assertThat(testMoratoriumItem.getMoratoriumItemTypeCode()).isEqualTo(DEFAULT_MORATORIUM_ITEM_TYPE_CODE);
        assertThat(testMoratoriumItem.getMoratoriumItemType()).isEqualTo(DEFAULT_MORATORIUM_ITEM_TYPE);
        assertThat(testMoratoriumItem.getMoratoriumTypeDetails()).isEqualTo(DEFAULT_MORATORIUM_TYPE_DETAILS);

        // Validate the MoratoriumItem in Elasticsearch
        verify(mockMoratoriumItemSearchRepository, times(1)).save(testMoratoriumItem);
    }

    @Test
    @Transactional
    void createMoratoriumItemWithExistingId() throws Exception {
        // Create the MoratoriumItem with an existing ID
        moratoriumItem.setId(1L);
        MoratoriumItemDTO moratoriumItemDTO = moratoriumItemMapper.toDto(moratoriumItem);

        int databaseSizeBeforeCreate = moratoriumItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoratoriumItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moratoriumItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoratoriumItem in the database
        List<MoratoriumItem> moratoriumItemList = moratoriumItemRepository.findAll();
        assertThat(moratoriumItemList).hasSize(databaseSizeBeforeCreate);

        // Validate the MoratoriumItem in Elasticsearch
        verify(mockMoratoriumItemSearchRepository, times(0)).save(moratoriumItem);
    }

    @Test
    @Transactional
    void checkMoratoriumItemTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = moratoriumItemRepository.findAll().size();
        // set the field null
        moratoriumItem.setMoratoriumItemTypeCode(null);

        // Create the MoratoriumItem, which fails.
        MoratoriumItemDTO moratoriumItemDTO = moratoriumItemMapper.toDto(moratoriumItem);

        restMoratoriumItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moratoriumItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<MoratoriumItem> moratoriumItemList = moratoriumItemRepository.findAll();
        assertThat(moratoriumItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMoratoriumItemTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = moratoriumItemRepository.findAll().size();
        // set the field null
        moratoriumItem.setMoratoriumItemType(null);

        // Create the MoratoriumItem, which fails.
        MoratoriumItemDTO moratoriumItemDTO = moratoriumItemMapper.toDto(moratoriumItem);

        restMoratoriumItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moratoriumItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<MoratoriumItem> moratoriumItemList = moratoriumItemRepository.findAll();
        assertThat(moratoriumItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMoratoriumItems() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        // Get all the moratoriumItemList
        restMoratoriumItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moratoriumItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].moratoriumItemTypeCode").value(hasItem(DEFAULT_MORATORIUM_ITEM_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].moratoriumItemType").value(hasItem(DEFAULT_MORATORIUM_ITEM_TYPE)))
            .andExpect(jsonPath("$.[*].moratoriumTypeDetails").value(hasItem(DEFAULT_MORATORIUM_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getMoratoriumItem() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        // Get the moratoriumItem
        restMoratoriumItemMockMvc
            .perform(get(ENTITY_API_URL_ID, moratoriumItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moratoriumItem.getId().intValue()))
            .andExpect(jsonPath("$.moratoriumItemTypeCode").value(DEFAULT_MORATORIUM_ITEM_TYPE_CODE))
            .andExpect(jsonPath("$.moratoriumItemType").value(DEFAULT_MORATORIUM_ITEM_TYPE))
            .andExpect(jsonPath("$.moratoriumTypeDetails").value(DEFAULT_MORATORIUM_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getMoratoriumItemsByIdFiltering() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        Long id = moratoriumItem.getId();

        defaultMoratoriumItemShouldBeFound("id.equals=" + id);
        defaultMoratoriumItemShouldNotBeFound("id.notEquals=" + id);

        defaultMoratoriumItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMoratoriumItemShouldNotBeFound("id.greaterThan=" + id);

        defaultMoratoriumItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMoratoriumItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMoratoriumItemsByMoratoriumItemTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        // Get all the moratoriumItemList where moratoriumItemTypeCode equals to DEFAULT_MORATORIUM_ITEM_TYPE_CODE
        defaultMoratoriumItemShouldBeFound("moratoriumItemTypeCode.equals=" + DEFAULT_MORATORIUM_ITEM_TYPE_CODE);

        // Get all the moratoriumItemList where moratoriumItemTypeCode equals to UPDATED_MORATORIUM_ITEM_TYPE_CODE
        defaultMoratoriumItemShouldNotBeFound("moratoriumItemTypeCode.equals=" + UPDATED_MORATORIUM_ITEM_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllMoratoriumItemsByMoratoriumItemTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        // Get all the moratoriumItemList where moratoriumItemTypeCode not equals to DEFAULT_MORATORIUM_ITEM_TYPE_CODE
        defaultMoratoriumItemShouldNotBeFound("moratoriumItemTypeCode.notEquals=" + DEFAULT_MORATORIUM_ITEM_TYPE_CODE);

        // Get all the moratoriumItemList where moratoriumItemTypeCode not equals to UPDATED_MORATORIUM_ITEM_TYPE_CODE
        defaultMoratoriumItemShouldBeFound("moratoriumItemTypeCode.notEquals=" + UPDATED_MORATORIUM_ITEM_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllMoratoriumItemsByMoratoriumItemTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        // Get all the moratoriumItemList where moratoriumItemTypeCode in DEFAULT_MORATORIUM_ITEM_TYPE_CODE or UPDATED_MORATORIUM_ITEM_TYPE_CODE
        defaultMoratoriumItemShouldBeFound(
            "moratoriumItemTypeCode.in=" + DEFAULT_MORATORIUM_ITEM_TYPE_CODE + "," + UPDATED_MORATORIUM_ITEM_TYPE_CODE
        );

        // Get all the moratoriumItemList where moratoriumItemTypeCode equals to UPDATED_MORATORIUM_ITEM_TYPE_CODE
        defaultMoratoriumItemShouldNotBeFound("moratoriumItemTypeCode.in=" + UPDATED_MORATORIUM_ITEM_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllMoratoriumItemsByMoratoriumItemTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        // Get all the moratoriumItemList where moratoriumItemTypeCode is not null
        defaultMoratoriumItemShouldBeFound("moratoriumItemTypeCode.specified=true");

        // Get all the moratoriumItemList where moratoriumItemTypeCode is null
        defaultMoratoriumItemShouldNotBeFound("moratoriumItemTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllMoratoriumItemsByMoratoriumItemTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        // Get all the moratoriumItemList where moratoriumItemTypeCode contains DEFAULT_MORATORIUM_ITEM_TYPE_CODE
        defaultMoratoriumItemShouldBeFound("moratoriumItemTypeCode.contains=" + DEFAULT_MORATORIUM_ITEM_TYPE_CODE);

        // Get all the moratoriumItemList where moratoriumItemTypeCode contains UPDATED_MORATORIUM_ITEM_TYPE_CODE
        defaultMoratoriumItemShouldNotBeFound("moratoriumItemTypeCode.contains=" + UPDATED_MORATORIUM_ITEM_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllMoratoriumItemsByMoratoriumItemTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        // Get all the moratoriumItemList where moratoriumItemTypeCode does not contain DEFAULT_MORATORIUM_ITEM_TYPE_CODE
        defaultMoratoriumItemShouldNotBeFound("moratoriumItemTypeCode.doesNotContain=" + DEFAULT_MORATORIUM_ITEM_TYPE_CODE);

        // Get all the moratoriumItemList where moratoriumItemTypeCode does not contain UPDATED_MORATORIUM_ITEM_TYPE_CODE
        defaultMoratoriumItemShouldBeFound("moratoriumItemTypeCode.doesNotContain=" + UPDATED_MORATORIUM_ITEM_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllMoratoriumItemsByMoratoriumItemTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        // Get all the moratoriumItemList where moratoriumItemType equals to DEFAULT_MORATORIUM_ITEM_TYPE
        defaultMoratoriumItemShouldBeFound("moratoriumItemType.equals=" + DEFAULT_MORATORIUM_ITEM_TYPE);

        // Get all the moratoriumItemList where moratoriumItemType equals to UPDATED_MORATORIUM_ITEM_TYPE
        defaultMoratoriumItemShouldNotBeFound("moratoriumItemType.equals=" + UPDATED_MORATORIUM_ITEM_TYPE);
    }

    @Test
    @Transactional
    void getAllMoratoriumItemsByMoratoriumItemTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        // Get all the moratoriumItemList where moratoriumItemType not equals to DEFAULT_MORATORIUM_ITEM_TYPE
        defaultMoratoriumItemShouldNotBeFound("moratoriumItemType.notEquals=" + DEFAULT_MORATORIUM_ITEM_TYPE);

        // Get all the moratoriumItemList where moratoriumItemType not equals to UPDATED_MORATORIUM_ITEM_TYPE
        defaultMoratoriumItemShouldBeFound("moratoriumItemType.notEquals=" + UPDATED_MORATORIUM_ITEM_TYPE);
    }

    @Test
    @Transactional
    void getAllMoratoriumItemsByMoratoriumItemTypeIsInShouldWork() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        // Get all the moratoriumItemList where moratoriumItemType in DEFAULT_MORATORIUM_ITEM_TYPE or UPDATED_MORATORIUM_ITEM_TYPE
        defaultMoratoriumItemShouldBeFound("moratoriumItemType.in=" + DEFAULT_MORATORIUM_ITEM_TYPE + "," + UPDATED_MORATORIUM_ITEM_TYPE);

        // Get all the moratoriumItemList where moratoriumItemType equals to UPDATED_MORATORIUM_ITEM_TYPE
        defaultMoratoriumItemShouldNotBeFound("moratoriumItemType.in=" + UPDATED_MORATORIUM_ITEM_TYPE);
    }

    @Test
    @Transactional
    void getAllMoratoriumItemsByMoratoriumItemTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        // Get all the moratoriumItemList where moratoriumItemType is not null
        defaultMoratoriumItemShouldBeFound("moratoriumItemType.specified=true");

        // Get all the moratoriumItemList where moratoriumItemType is null
        defaultMoratoriumItemShouldNotBeFound("moratoriumItemType.specified=false");
    }

    @Test
    @Transactional
    void getAllMoratoriumItemsByMoratoriumItemTypeContainsSomething() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        // Get all the moratoriumItemList where moratoriumItemType contains DEFAULT_MORATORIUM_ITEM_TYPE
        defaultMoratoriumItemShouldBeFound("moratoriumItemType.contains=" + DEFAULT_MORATORIUM_ITEM_TYPE);

        // Get all the moratoriumItemList where moratoriumItemType contains UPDATED_MORATORIUM_ITEM_TYPE
        defaultMoratoriumItemShouldNotBeFound("moratoriumItemType.contains=" + UPDATED_MORATORIUM_ITEM_TYPE);
    }

    @Test
    @Transactional
    void getAllMoratoriumItemsByMoratoriumItemTypeNotContainsSomething() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        // Get all the moratoriumItemList where moratoriumItemType does not contain DEFAULT_MORATORIUM_ITEM_TYPE
        defaultMoratoriumItemShouldNotBeFound("moratoriumItemType.doesNotContain=" + DEFAULT_MORATORIUM_ITEM_TYPE);

        // Get all the moratoriumItemList where moratoriumItemType does not contain UPDATED_MORATORIUM_ITEM_TYPE
        defaultMoratoriumItemShouldBeFound("moratoriumItemType.doesNotContain=" + UPDATED_MORATORIUM_ITEM_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMoratoriumItemShouldBeFound(String filter) throws Exception {
        restMoratoriumItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moratoriumItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].moratoriumItemTypeCode").value(hasItem(DEFAULT_MORATORIUM_ITEM_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].moratoriumItemType").value(hasItem(DEFAULT_MORATORIUM_ITEM_TYPE)))
            .andExpect(jsonPath("$.[*].moratoriumTypeDetails").value(hasItem(DEFAULT_MORATORIUM_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restMoratoriumItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMoratoriumItemShouldNotBeFound(String filter) throws Exception {
        restMoratoriumItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMoratoriumItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMoratoriumItem() throws Exception {
        // Get the moratoriumItem
        restMoratoriumItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMoratoriumItem() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        int databaseSizeBeforeUpdate = moratoriumItemRepository.findAll().size();

        // Update the moratoriumItem
        MoratoriumItem updatedMoratoriumItem = moratoriumItemRepository.findById(moratoriumItem.getId()).get();
        // Disconnect from session so that the updates on updatedMoratoriumItem are not directly saved in db
        em.detach(updatedMoratoriumItem);
        updatedMoratoriumItem
            .moratoriumItemTypeCode(UPDATED_MORATORIUM_ITEM_TYPE_CODE)
            .moratoriumItemType(UPDATED_MORATORIUM_ITEM_TYPE)
            .moratoriumTypeDetails(UPDATED_MORATORIUM_TYPE_DETAILS);
        MoratoriumItemDTO moratoriumItemDTO = moratoriumItemMapper.toDto(updatedMoratoriumItem);

        restMoratoriumItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moratoriumItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moratoriumItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the MoratoriumItem in the database
        List<MoratoriumItem> moratoriumItemList = moratoriumItemRepository.findAll();
        assertThat(moratoriumItemList).hasSize(databaseSizeBeforeUpdate);
        MoratoriumItem testMoratoriumItem = moratoriumItemList.get(moratoriumItemList.size() - 1);
        assertThat(testMoratoriumItem.getMoratoriumItemTypeCode()).isEqualTo(UPDATED_MORATORIUM_ITEM_TYPE_CODE);
        assertThat(testMoratoriumItem.getMoratoriumItemType()).isEqualTo(UPDATED_MORATORIUM_ITEM_TYPE);
        assertThat(testMoratoriumItem.getMoratoriumTypeDetails()).isEqualTo(UPDATED_MORATORIUM_TYPE_DETAILS);

        // Validate the MoratoriumItem in Elasticsearch
        verify(mockMoratoriumItemSearchRepository).save(testMoratoriumItem);
    }

    @Test
    @Transactional
    void putNonExistingMoratoriumItem() throws Exception {
        int databaseSizeBeforeUpdate = moratoriumItemRepository.findAll().size();
        moratoriumItem.setId(count.incrementAndGet());

        // Create the MoratoriumItem
        MoratoriumItemDTO moratoriumItemDTO = moratoriumItemMapper.toDto(moratoriumItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoratoriumItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moratoriumItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moratoriumItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoratoriumItem in the database
        List<MoratoriumItem> moratoriumItemList = moratoriumItemRepository.findAll();
        assertThat(moratoriumItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MoratoriumItem in Elasticsearch
        verify(mockMoratoriumItemSearchRepository, times(0)).save(moratoriumItem);
    }

    @Test
    @Transactional
    void putWithIdMismatchMoratoriumItem() throws Exception {
        int databaseSizeBeforeUpdate = moratoriumItemRepository.findAll().size();
        moratoriumItem.setId(count.incrementAndGet());

        // Create the MoratoriumItem
        MoratoriumItemDTO moratoriumItemDTO = moratoriumItemMapper.toDto(moratoriumItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoratoriumItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moratoriumItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoratoriumItem in the database
        List<MoratoriumItem> moratoriumItemList = moratoriumItemRepository.findAll();
        assertThat(moratoriumItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MoratoriumItem in Elasticsearch
        verify(mockMoratoriumItemSearchRepository, times(0)).save(moratoriumItem);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMoratoriumItem() throws Exception {
        int databaseSizeBeforeUpdate = moratoriumItemRepository.findAll().size();
        moratoriumItem.setId(count.incrementAndGet());

        // Create the MoratoriumItem
        MoratoriumItemDTO moratoriumItemDTO = moratoriumItemMapper.toDto(moratoriumItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoratoriumItemMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moratoriumItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoratoriumItem in the database
        List<MoratoriumItem> moratoriumItemList = moratoriumItemRepository.findAll();
        assertThat(moratoriumItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MoratoriumItem in Elasticsearch
        verify(mockMoratoriumItemSearchRepository, times(0)).save(moratoriumItem);
    }

    @Test
    @Transactional
    void partialUpdateMoratoriumItemWithPatch() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        int databaseSizeBeforeUpdate = moratoriumItemRepository.findAll().size();

        // Update the moratoriumItem using partial update
        MoratoriumItem partialUpdatedMoratoriumItem = new MoratoriumItem();
        partialUpdatedMoratoriumItem.setId(moratoriumItem.getId());

        partialUpdatedMoratoriumItem
            .moratoriumItemType(UPDATED_MORATORIUM_ITEM_TYPE)
            .moratoriumTypeDetails(UPDATED_MORATORIUM_TYPE_DETAILS);

        restMoratoriumItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoratoriumItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMoratoriumItem))
            )
            .andExpect(status().isOk());

        // Validate the MoratoriumItem in the database
        List<MoratoriumItem> moratoriumItemList = moratoriumItemRepository.findAll();
        assertThat(moratoriumItemList).hasSize(databaseSizeBeforeUpdate);
        MoratoriumItem testMoratoriumItem = moratoriumItemList.get(moratoriumItemList.size() - 1);
        assertThat(testMoratoriumItem.getMoratoriumItemTypeCode()).isEqualTo(DEFAULT_MORATORIUM_ITEM_TYPE_CODE);
        assertThat(testMoratoriumItem.getMoratoriumItemType()).isEqualTo(UPDATED_MORATORIUM_ITEM_TYPE);
        assertThat(testMoratoriumItem.getMoratoriumTypeDetails()).isEqualTo(UPDATED_MORATORIUM_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateMoratoriumItemWithPatch() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        int databaseSizeBeforeUpdate = moratoriumItemRepository.findAll().size();

        // Update the moratoriumItem using partial update
        MoratoriumItem partialUpdatedMoratoriumItem = new MoratoriumItem();
        partialUpdatedMoratoriumItem.setId(moratoriumItem.getId());

        partialUpdatedMoratoriumItem
            .moratoriumItemTypeCode(UPDATED_MORATORIUM_ITEM_TYPE_CODE)
            .moratoriumItemType(UPDATED_MORATORIUM_ITEM_TYPE)
            .moratoriumTypeDetails(UPDATED_MORATORIUM_TYPE_DETAILS);

        restMoratoriumItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoratoriumItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMoratoriumItem))
            )
            .andExpect(status().isOk());

        // Validate the MoratoriumItem in the database
        List<MoratoriumItem> moratoriumItemList = moratoriumItemRepository.findAll();
        assertThat(moratoriumItemList).hasSize(databaseSizeBeforeUpdate);
        MoratoriumItem testMoratoriumItem = moratoriumItemList.get(moratoriumItemList.size() - 1);
        assertThat(testMoratoriumItem.getMoratoriumItemTypeCode()).isEqualTo(UPDATED_MORATORIUM_ITEM_TYPE_CODE);
        assertThat(testMoratoriumItem.getMoratoriumItemType()).isEqualTo(UPDATED_MORATORIUM_ITEM_TYPE);
        assertThat(testMoratoriumItem.getMoratoriumTypeDetails()).isEqualTo(UPDATED_MORATORIUM_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingMoratoriumItem() throws Exception {
        int databaseSizeBeforeUpdate = moratoriumItemRepository.findAll().size();
        moratoriumItem.setId(count.incrementAndGet());

        // Create the MoratoriumItem
        MoratoriumItemDTO moratoriumItemDTO = moratoriumItemMapper.toDto(moratoriumItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoratoriumItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moratoriumItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(moratoriumItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoratoriumItem in the database
        List<MoratoriumItem> moratoriumItemList = moratoriumItemRepository.findAll();
        assertThat(moratoriumItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MoratoriumItem in Elasticsearch
        verify(mockMoratoriumItemSearchRepository, times(0)).save(moratoriumItem);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMoratoriumItem() throws Exception {
        int databaseSizeBeforeUpdate = moratoriumItemRepository.findAll().size();
        moratoriumItem.setId(count.incrementAndGet());

        // Create the MoratoriumItem
        MoratoriumItemDTO moratoriumItemDTO = moratoriumItemMapper.toDto(moratoriumItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoratoriumItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(moratoriumItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoratoriumItem in the database
        List<MoratoriumItem> moratoriumItemList = moratoriumItemRepository.findAll();
        assertThat(moratoriumItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MoratoriumItem in Elasticsearch
        verify(mockMoratoriumItemSearchRepository, times(0)).save(moratoriumItem);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMoratoriumItem() throws Exception {
        int databaseSizeBeforeUpdate = moratoriumItemRepository.findAll().size();
        moratoriumItem.setId(count.incrementAndGet());

        // Create the MoratoriumItem
        MoratoriumItemDTO moratoriumItemDTO = moratoriumItemMapper.toDto(moratoriumItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoratoriumItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(moratoriumItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoratoriumItem in the database
        List<MoratoriumItem> moratoriumItemList = moratoriumItemRepository.findAll();
        assertThat(moratoriumItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MoratoriumItem in Elasticsearch
        verify(mockMoratoriumItemSearchRepository, times(0)).save(moratoriumItem);
    }

    @Test
    @Transactional
    void deleteMoratoriumItem() throws Exception {
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);

        int databaseSizeBeforeDelete = moratoriumItemRepository.findAll().size();

        // Delete the moratoriumItem
        restMoratoriumItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, moratoriumItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MoratoriumItem> moratoriumItemList = moratoriumItemRepository.findAll();
        assertThat(moratoriumItemList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MoratoriumItem in Elasticsearch
        verify(mockMoratoriumItemSearchRepository, times(1)).deleteById(moratoriumItem.getId());
    }

    @Test
    @Transactional
    void searchMoratoriumItem() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        moratoriumItemRepository.saveAndFlush(moratoriumItem);
        when(mockMoratoriumItemSearchRepository.search("id:" + moratoriumItem.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(moratoriumItem), PageRequest.of(0, 1), 1));

        // Search the moratoriumItem
        restMoratoriumItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + moratoriumItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moratoriumItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].moratoriumItemTypeCode").value(hasItem(DEFAULT_MORATORIUM_ITEM_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].moratoriumItemType").value(hasItem(DEFAULT_MORATORIUM_ITEM_TYPE)))
            .andExpect(jsonPath("$.[*].moratoriumTypeDetails").value(hasItem(DEFAULT_MORATORIUM_TYPE_DETAILS.toString())));
    }
}
